from django.db import models
from django.contrib.auth.models import User

# Create your models here.

class User_key(models.Model):
    user = models.OneToOneField(User)
    user_key = models.BinaryField(max_length=200)


class Devices(models.Model):
    user = models.ManyToManyField(User)
    device_key = models.BinaryField(max_length=200)
