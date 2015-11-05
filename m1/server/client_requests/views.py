import json
from Crypto.Cipher import AES

from rest_framework import status
from rest_framework.decorators import api_view
from django.contrib.auth.models import User
from django.http import HttpResponse
from users_data.models import User_key, Purchases, Devices
from books.models import Book
from player.models import Player


@api_view(['POST'])
def read_book(request):
    book_id = request.POST.get('book_id')
    # email = request.POST.get('user')
    #
    # if email==None or email=="":
    #     return HttpResponse("Invalid email", status=status.HTTP_400_BAD_REQUEST)
    #
    # user = User.objects.get(email=email)
    #
    # if user==None:
    #     return HttpResponse("User doesn't exist", status=status.HTTP_400_BAD_REQUEST)

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
                    "device_key":"",
                    "player_key":"",
                    "location":"",
                    "so":"",
                    "time":"",
                    }

    return HttpResponse(json.dumps(restrictions), status=status.HTTP_200_OK)


@api_view(['POST'])
def validate(request):
    pass


# code adapted from http://stackoverflow.com/questions/12524994/encrypt-decrypt-using-pycrypto-aes-256
@api_view(['POST'])
def decrypt(request):
    keys = request.POST.get('key')

    if not request.user.is_authenticated():
        return HttpResponse("User not logged in", status=status.HTTP_403_FORBIDDEN)







