import os
import json
from rest_framework import status
from rest_framework.decorators import api_view
from rest_framework.response import Response
from django.contrib.auth.models import User
from django.contrib.auth import authenticate, login, logout
from django.core.exceptions import ValidationError
from django.core.validators import validate_email
from django.shortcuts import render, redirect
from django.http import HttpResponse
from users_data.models import User_key, Purchases, Devices
from books.models import Book



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

    user_key = os.urandom(128)

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
    template = 'index.html'
    email = request.POST['email']
    email = email[1:-1]

    book_id = request.POST['book_id']

    book = Book.objects.get(ebook_id=book_id)

    if book is None:
        return Response("Book doesn't exists", status=status.HTTP_400_BAD_REQUEST)


    user = User.objects.get(email=email)

    if user is None:
        return Response("User doesn't exists", status=status.HTTP_400_BAD_REQUEST)


    random = os.urandom(128)

    purchase = Purchases(user=user, book_id=book, random=random)

    purchase.save()

    return render(request, template)


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

    return HttpResponse("Device added successfully", status=status.HTTP_200_OK)



