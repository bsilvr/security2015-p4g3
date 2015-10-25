from rest_framework import serializers
from books.models import Book

"""
Code adapted from django rest_framework tutorials
http://www.django-rest-framework.org/tutorial/1-serialization/
"""

class BookSerializer(serializers.Serializer):
    ebook_id = serializers.IntegerField(read_only=True)
    author = serializers.CharField(required=True, max_length=100)
    title = serializers.CharField(required=True, max_length=100)
    language = serializers.CharField(required=True, max_length=100)
    cover = serializers.CharField(required=True, max_length=200)

    def create(self, validated_data):
        """
        Create and return a new `Book` instance, given the validated data.
        """
        return Book.objects.create(**validated_data)

    def update(self, instance, validated_data):
        """
        Update and return an existing `Book` instance, given the validated data.
        """
        instance.author = validated_data.get('author', instance.author)
        instance.title = validated_data.get('title', instance.title)
        instance.language = validated_data.get('language', instance.language)
        instance.cover = validated_data.get('cover', instance.cover)
        instance.save()
        return instance
