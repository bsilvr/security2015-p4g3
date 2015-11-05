import os
import json
from rest_framework import status
from rest_framework.decorators import api_view
from rest_framework.response import Response
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


    if book_id==None or book_id=="":
        return HttpResponse("Invalid book_id", status=status.HTTP_400_BAD_REQUEST)

    book = Book.objects.get(ebook_id=book_id)

    if book is None:
        return Response("Book doesn't exists", status=status.HTTP_400_BAD_REQUEST)

    


