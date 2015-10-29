from django.db import models
from django.contrib.auth.models import User
from books.models import Book
from player.models import Player

# Create your models here.

class User_key(models.Model):
    user = models.OneToOneField(User, primary_key=True)
    user_key = models.BinaryField(max_length=128)
    player_used = models.ForeignKey(Player, null=True)

class Devices(models.Model):
    user = models.ManyToManyField(User)
    device_key = models.BinaryField(max_length=128)
    device_name = models.CharField(max_length=250)

class Purchases(models.Model):
    user = models.ForeignKey(User)
    book_id = models.ForeignKey(Book)
    random = models.BinaryField(max_length=128)
    file_key = models.BinaryField(max_length=128)
