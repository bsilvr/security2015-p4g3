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

@api_view(['GET'])
def get_book(request):
    if request.method == 'GET':
        book_id = request.GET.get('book_id')
        print book_id
        if book_id == None or book_id == "":
            return Response("Argument book cannot be empty", status=status.HTTP_400_BAD_REQUEST)

        try:
            book = Book.objects.get(ebook_id=book_id)
        except:
            return Response("Book with that argument doesn't exist in the database", status=status.HTTP_400_BAD_REQUEST)

        serializer = BookSerializer(book, many=False)

        return Response(JSONRenderer().render(serializer.data), status=status.HTTP_200_OK)

