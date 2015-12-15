from rest_framework import serializers
from users_data.models import Purchases
"""
Code adapted from django rest_framework tutorials
http://www.django-rest-framework.org/tutorial/1-serialization/
"""

class PurchasesSerializer(serializers.Serializer):
    user = serializers.CharField(read_only=True)
    book_id = serializers.IntegerField(required=True)

    def create(self, validated_data):
        """
        Create and return a new `Book` instance, given the validated data.
        """
        return Purchases.objects.create(**validated_data)

    def update(self, instance, validated_data):
        """
        Update and return an existing `Book` instance, given the validated data.
        """
        instance.user = validated_data.get('user', instance.user)
        instance.book_id = validated_data.get('book_id', instance.book_id)
        instance.save()
        return instance
