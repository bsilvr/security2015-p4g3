from django.db import models
from django.contrib.auth.models import User

# Create your models here.

class User_key(models.Model):
    user = models.OneToOneField(User)
    user_key = models.CharField(max_length=200)

class Player
