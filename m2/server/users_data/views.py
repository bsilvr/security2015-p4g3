import os
import json
import base64

from rest_framework import status
from rest_framework.decorators import api_view
from rest_framework.response import Response
from django.contrib.auth.models import User
from django.contrib.auth import authenticate, login, logout
from django.core.exceptions import ValidationError
from django.core.validators import validate_email
from django.shortcuts import redirect
from django.http import HttpResponse

from users_data.models import User_key, Purchases, Devices, PteIdLogin
from books.models import Book

from Crypto.Hash import SHA
from Crypto.PublicKey import RSA
from Crypto.Signature import PKCS1_v1_5



# code adapted from: https://docs.djangoproject.com/en/1.8/topics/auth/default/
@api_view(['POST'])
def create_user(request):
    password = request.POST['password']
    confirm = request.POST['confirm']
    email = request.POST['email']
    fname = request.POST['first_name']
    lname = request.POST['last_name']

    u = User.objects.all().filter(email=email)

    if len(u) != 0:
        return Response('User Already Exists', status=status.HTTP_400_BAD_REQUEST)

    try:
        validate_email(email)
    except ValidationError as e:
        return Response('Invalid email', status=status.HTTP_400_BAD_REQUEST)

    if password != confirm:
        return Response("Passwords don't match", status=status.HTTP_400_BAD_REQUEST)


    user = User.objects.create_user(email, email=email, password=password)
    user.first_name = fname
    user.last_name = lname

    user.save()

    user_key = os.urandom(16)

    user_key = base64.b64encode(user_key)

    user_extra_info = User_key(user=user, user_key=user_key)

    user_extra_info.save()

    user = authenticate(username=email, password=password)

    if user is not None:
        if user.is_active:
            login(request, user)
            response = redirect('/')
            response.set_cookie(key='fname', value=user.first_name)
            response.set_cookie(key='email', value=user.email)

            return response

    return Response('Invalid email or password', status=status.HTTP_400_BAD_REQUEST)


# code adapted from: https://docs.djangoproject.com/en/1.8/topics/auth/default/
@api_view(['POST'])
def user_login(request):
    email = request.POST['email']
    password = request.POST['password']
    user = authenticate(username=email, password=password)

    if user is not None:
        if user.is_active:
            login(request, user)
            response = redirect('/')
            response.set_cookie(key='fname', value=user.first_name)
            response.set_cookie(key='email', value=user.email)

            return response
        else:
            return Response('Account Disabled', status=status.HTTP_400_BAD_REQUEST)

    else:
        return Response('Invalid email or password', status=status.HTTP_400_BAD_REQUEST)

@api_view(['POST'])
def user_logout(request):
    logout(request)
    response = redirect('/')
    response.set_cookie(key='fname', value="")
    response.set_cookie(key='email', value="")

    return response



@api_view(['POST'])
def buy_book(request):
    book_id = request.POST['book_id']

    book = Book.objects.get(ebook_id=book_id)

    if not request.user.is_authenticated():
        return HttpResponse("User not logged in", status=status.HTTP_403_FORBIDDEN)

    if book is None:
        return Response("Book doesn't exists", status=status.HTTP_400_BAD_REQUEST)


    random = os.urandom(16)

    random = base64.b64encode(random)


    purchase = Purchases(user=request.user, book_id=book, random=random)

    purchase.save()

    return HttpResponse("Success", status=status.HTTP_200_OK)


@api_view(['POST'])
def get_purchases(request):
    email = request.POST.get('user')

    if email == "" or email is None:
        return Response('Email was empty', status=status.HTTP_400_BAD_REQUEST)

    user_id = User.objects.get(email=email)

    purchases = Purchases.objects.all().filter(user=user_id)
    j = []
    for i in purchases:
        p = {'user': i.user.email, 'book_id': i.book_id.ebook_id}
        j.append(p)

    return HttpResponse(json.dumps(j), status=status.HTTP_200_OK)


@api_view(['POST'])
def register_device(request):
    email = request.POST.get('user')
    device_key = request.POST.get('device_key')
    device_name = request.POST.get('device_name')

    if device_key==None or device_key=="":
        return HttpResponse("Invalid device key", status=status.HTTP_400_BAD_REQUEST)

    if email==None or email=="":
        return HttpResponse("Invalid email", status=status.HTTP_400_BAD_REQUEST)

    user = User.objects.get(email=email)

    if user==None:
        return HttpResponse("Invalid User", status=status.HTTP_400_BAD_REQUEST)

    if device_name==None or device_name=="":
        device_name = user.first_name + "'s Computer"

    d = Devices.objects.all().filter(device_key=device_key)

    if len(d):
        d = d[0]
        for u in d.user.all():
            if u.email == email:
                return HttpResponse("Device key already linked to that user", status=status.HTTP_400_BAD_REQUEST)


    device = Devices(device_key=device_key, device_name=device_name)

    device.save()

    device.user.add(user)

    return Response("Device added successfully", status=status.HTTP_200_OK)


@api_view(['POST'])
def addCC(request):
    pub_key = request.POST.get('public_key')

    if not request.user.is_authenticated():
        return HttpResponse("User not logged in", status=status.HTTP_403_FORBIDDEN)

    user = request.user

    if pub_key == "" or pub_key == None:
        return HttpResponse("Empty public key", status=status.HTTP_400_BAD_REQUEST)

    if not pub_key.startswith("-----BEGIN PUBLIC KEY-----") and not pub_key.endswith("-----END PUBLIC KEY-----"):
        return HttpResponse("Invalid public key", status=status.HTTP_400_BAD_REQUEST)

    if not user.user_key.public_key == None:
        return HttpResponse("User already has public key associated", status=status.HTTP_202_ACCEPTED)

    user = User_key.objects.get(user=user)

    user.public_key = pub_key

    user.save()

    return Response("Public key added successfully", status=status.HTTP_200_OK)


@api_view(['POST'])
def loginCC(request):
    email = request.POST.get('user')

    if email==None or email=="":
        return HttpResponse("Invalid email", status=status.HTTP_400_BAD_REQUEST)


    user = User.objects.get(email=email)
    user_key = User_key.objects.get(user=user)

    if user == None:
        return HttpResponse("User doesnt exist", status=status.HTTP_400_BAD_REQUEST)

    if user_key.public_key == None:
        return HttpResponse("User hasnt linked CC", status=status.HTTP_400_BAD_REQUEST)

    random = os.urandom(128)

    randomb64 = base64.b64encode(random)

    transaction = PteIdLogin(random=randomb64, user=user)

    transaction.save()

    response = HttpResponse("Challenge sent", status=status.HTTP_200_OK)

    response["random"] = randomb64
    response["transactionID"] = transaction.transactionId

    return response


@api_view(['POST'])
def validateLoginCC(request):
    transactionId = request.POST.get('transactionId')
    signed = request.POST.get('signed')

    if transactionId==None:
        return HttpResponse("Invalid transaction", status=status.HTTP_400_BAD_REQUEST)

    transaction = PteIdLogin.objects.get(transactionId=transactionId)

    if transaction==None:
        return HttpResponse("Invalid transaction", status=status.HTTP_400_BAD_REQUEST)

    user = User.objects.get(username=transaction.user.username)

    pub_key = user.user_key.public_key

    random = base64.b64decode(transaction.random)

    signature = base64.b64decode(signed)

    key = RSA.importKey(pub_key)
    h = SHA.new(random)
    verifier = PKCS1_v1_5.new(key)

    if verifier.verify(h, signature):
        user = authenticate(username=user.username)
        login(request, user)
        return HttpResponse("Log in successfully", status=status.HTTP_200_OK)
    else:
        return HttpResponse("Invalid Signature", status=status.HTTP_400_BAD_REQUEST)



