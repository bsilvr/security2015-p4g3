from django.db import models

# Create your models here.

class Player(models.Model):
    player_key = models.BinaryField(max_length=200)
    app_file = models.CharField(max_length=200)
    signature = models.BinaryField(max_length=200)
