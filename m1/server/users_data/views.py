import os
from rest_framework import status
from rest_framework.decorators import api_view
from rest_framework.response import Response
from django.contrib.auth.models import User
from django.contrib.auth import authenticate, login, logout
from django.core.exceptions import ValidationError
from django.core.validators import validate_email
from django.shortcuts import render
from users_data.models import User_key




# code adapted from: https://docs.djangoproject.com/en/1.8/topics/auth/default/
@api_view(['POST'])
def create_user(request):
    password = request.POST['password']
    confirm = request.POST['confirm']
    email = request.POST['email']
    fname = request.POST['first_name']
    lname = request.POST['last_name']

    try:
        validate_email(email)
    except ValidationError as e:
        return Response('Invalid email', status=status.HTTP_406_NOT_ACCEPTABLE)

    if password != confirm:
        return Response("Passwords don't match", status=status.HTTP_406_NOT_ACCEPTABLE)


    user = User.objects.create_user(email, email=email, password=password)
    user.first_name = fname
    user.last_name = lname

    user.save()

    user_key = os.urandom(128)

    user_extra_info = User_key(user=user, user_key=user_key)

    user_extra_info.save()

    return Response('User created', status=status.HTTP_200_OK)



# code adapted from: https://docs.djangoproject.com/en/1.8/topics/auth/default/
@api_view(['POST'])
def user_login(request):
    email = request.POST['email']
    password = request.POST['password']
    user = authenticate(username=email, password=password)
    template = 'index.html'

    if user is not None:
        if user.is_active:
            login(request, user)
            response = render(request, template)
            response.set_cookie(key='fname', value=user.first_name)
            response.set_cookie(key='email', value=user.email)

            return response
        else:
            return Response('Account Disabled', status=status.HTTP_406_NOT_ACCEPTABLE)

    else:
        return Response('Invalid email or password', status=status.HTTP_406_NOT_ACCEPTABLE)

@api_view(['POST'])
def user_logout(request):
    template = 'index.html'
    logout(request)
    response = render(request, template)
    response.set_cookie(key='fname', value="")
    response.set_cookie(key='email', value="")

    return response




def buybook(request):
    pass
