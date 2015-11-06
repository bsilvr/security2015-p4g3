from django.db import models

# Create your models here.

class Book(models.Model):
    ebook_id = models.IntegerField(primary_key=True)
    author = models.CharField(max_length=100)
    title = models.CharField(max_length=100)
    language = models.CharField(max_length=100)
    cover = models.CharField(max_length=200)
    text_file = models.CharField(max_length=300)
