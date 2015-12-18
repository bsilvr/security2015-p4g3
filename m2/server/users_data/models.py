from django.db import models
from django.contrib.auth.models import User
from books.models import Book
from player.models import Player

# Create your models here.

class User_key(models.Model):
    user = models.OneToOneField(User, primary_key=True)
    user_key = models.CharField(max_length=128)
    public_key = models.CharField(max_length=400, blank=True, null=True, default=None)

class Devices(models.Model):
    user = models.ManyToManyField(User)
    device_key = models.CharField(max_length=128)
    device_name = models.CharField(max_length=250)

class Purchases(models.Model):
    user = models.ForeignKey(User)
    book_id = models.ForeignKey(Book)
    random = models.CharField(max_length=128)

class PteIdLogin(models.Model):
    transactionId = models.AutoField(primary_key=True)
    random = models.CharField(max_length=200)
    user = models.ForeignKey(User)