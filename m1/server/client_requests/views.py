import json
import os
import base64
from Crypto.Cipher import AES

from rest_framework import status
from rest_framework.decorators import api_view
from django.http import HttpResponse
from users_data.models import Purchases, Devices
from books.models import Book
from player.models import Player
from restrictions.models import *



@api_view(['POST'])
def read_book(request):
    book_id = request.POST.get('book_id')

    if not request.user.is_authenticated():
        return HttpResponse("User not logged in", status=status.HTTP_403_FORBIDDEN)


    if book_id==None or book_id=="":
        return HttpResponse("Invalid book_id", status=status.HTTP_400_BAD_REQUEST)

    book = Book.objects.get(ebook_id=book_id)

    if book is None:
        return HttpResponse("Book doesn't exists", status=status.HTTP_400_BAD_REQUEST)

    p = Purchases.objects.all().filter(user=request.user, book_id=book)

    if len(p) == 0:
        return HttpResponse("User hasn't bought the book", status=status.HTTP_403_FORBIDDEN)


    restrictions = {
                    "book_id":"",
                    "device_key":"",
                    "player_key":"",
                    "location":"",
                    "so":"",
                    "time":"",
                    }

    return HttpResponse(json.dumps(restrictions), status=status.HTTP_200_OK)


# consulted code from: http://eli.thegreenplace.net/2010/06/25/aes-encryption-of-files-in-python-with-pycrypto
# consulted code from http://stackoverflow.com/questions/12524994/encrypt-decrypt-using-pycrypto-aes-256

@api_view(['POST'])
def validate(request):
    book_id = request.POST.get('book_id')
    device_key = request.POST.get('device_key')
    player_key = request.POST.get('player_key')
    location = request.POST.get('location')
    so = request.POST.get('so')
    time = request.POST.get('time')

    # Validating reading restrictions

    if not request.user.is_authenticated():
        return HttpResponse("User not logged in", status=status.HTTP_403_FORBIDDEN)


    if book_id==None or book_id=="":
        return HttpResponse("Invalid book_id", status=status.HTTP_400_BAD_REQUEST)

    book = Book.objects.get(ebook_id=book_id)

    if book is None:
        return HttpResponse("Book doesn't exists", status=status.HTTP_400_BAD_REQUEST)


    if device_key==None or device_key=="":
        return HttpResponse("Invalid device key", status=status.HTTP_400_BAD_REQUEST)

    device = Devices.objects.get(device_key=device_key)

    if device is None:
        return HttpResponse("Device is not registered", status=status.HTTP_400_BAD_REQUEST)


    if player_key==None or player_key=="":
        return HttpResponse("Invalid player key", status=status.HTTP_400_BAD_REQUEST)

    player = Player.objects.get(player_key=player_key)

    if player is None:
        return HttpResponse("Player doesn't exist", status=status.HTTP_400_BAD_REQUEST)


    if location==None or location=="":
        return HttpResponse("Invalid location", status=status.HTTP_400_BAD_REQUEST)

    country = Countries.objects.get(country=location)

    if country is None:
        return HttpResponse("User not allowed to read the book in the country he's in", status=status.HTTP_403_FORBIDDEN)


    if so==None or so=="":
        return HttpResponse("Invalid operating system", status=status.HTTP_400_BAD_REQUEST)

    system = OperatingSystem.objects.get(so=so)

    if system is None:
        return HttpResponse("User not allowed to read the book with his operating system", status=status.HTTP_403_FORBIDDEN)


    if time==None or time=="" or len(time.split(':')) != 3:
        return HttpResponse("Invalid time", status=status.HTTP_400_BAD_REQUEST)

    hour = time.split(':')[0]

    if int(hour) < 0 or int(hour) > 24:
        return HttpResponse("User not allowed to read the book at this time", status=status.HTTP_403_FORBIDDEN)

    # End of validating reading restrictions

    # Ciphering the book for the user

    random = Purchases.objects.all().filter(user=request.user, book_id=book)[0].random
    user_key = request.user.user_key.user_key

    IV = 'oObVMqPyzcRzWvyB'


    # cipher random with player key
    aes = AES.new(player_key, AES.MODE_CBC, IV)
    key1 = aes.encrypt(random)

    # cipher previous key with user_key
    aes = AES.new(user_key, AES.MODE_CBC, IV)
    key2 = aes.encrypt(key1)

    # cipher previous key with device_key
    aes = AES.new(device_key, AES.MODE_CBC, IV)
    file_key = aes.encrypt(key2)

    # end result is cipher(device_key, cipher(user_key, cipher(player_key, random)))

    # starting to cipher file using file key

    BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))

    file_path = os.path.join(BASE_DIR, book.text_file)
    cipher_file_path = file_path.replace("books", "cipher_books")

    with open(file_path, 'rb') as book_file:
        with open(cipher_file_path, 'w+') as cipher_file:
            block_size = 16
            bytes_read = 0
            data = book_file.read()
            length = 16 - (len(data) % 16)
            data += chr(length)*length
            size = len(data)

            aes = AES.new(file_key, AES.MODE_CBC, IV)
            while bytes_read < size:
                block = data[0:block_size]
                data = data[block_size:]
                bytes_read += block_size

                ciphered_block = aes.encrypt(block)

                cipher_file.write(ciphered_block)


    response = HttpResponse("Successfully ciphered", status=status.HTTP_200_OK)

    response['random'] = base64.b64encode(random)

    for c in random:
        print ord(c),
    print
    print response['random']

    return response


@api_view(['POST'])
def get_file(request):
    book_id = request.POST.get('book_id')
    print "ddd"

    if not request.user.is_authenticated():
        return HttpResponse("User not logged in", status=status.HTTP_403_FORBIDDEN)


    if book_id==None or book_id=="":
        return HttpResponse("Invalid book_id", status=status.HTTP_400_BAD_REQUEST)

    book = Book.objects.get(ebook_id=book_id)

    if book is None:
        return HttpResponse("Book doesn't exists", status=status.HTTP_400_BAD_REQUEST)


    file_path = "media/cipher_books/pg" + book_id + ".txt"
    print file_path

    cipher_file = open(file_path, 'rb')

    cipher_content = cipher_file.read()

    cipher_file.close()

    os.remove(file_path)

    return HttpResponse(cipher_content, content_type='text/plain', status=status.HTTP_200_OK)


@api_view(['POST'])
def decrypt(request):
    key = request.POST.get('Key')
    key= base64.b64decode(key)

    if not request.user.is_authenticated():
        return HttpResponse("User not logged in", status=status.HTTP_403_FORBIDDEN)


    user_key = request.user.user_key.user_key

    IV = 'oObVMqPyzcRzWvyB'

    aes = AES.new(user_key, AES.MODE_CBC, IV)
    encryptedKey = aes.encrypt(key)

    encryptedKey_b64 = base64.b16encode(encryptedKey)

    return HttpResponse(encryptedKey_b64, status=status.HTTP_200_OK)
