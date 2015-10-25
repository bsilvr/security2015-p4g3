from django.shortcuts import render
from rest_framework import status
from rest_framework.decorators import api_view
from rest_framework.response import Response
from books.models import Book
from books.serializers import BookSerializer
from rest_framework.renderers import JSONRenderer

# Create your views here.
@api_view(['GET'])
def get_books(request):
    if request.method == 'GET':
        books = Book.objects.all()
        serializer = BookSerializer(books, many=True)
        return Response(JSONRenderer().render(serializer.data), status=status.HTTP_200_OK)
