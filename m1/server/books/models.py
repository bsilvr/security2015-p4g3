from django.db import models

# Create your models here.

class Book(models.Model):
    ebook_id = models.IntegerField(primary_key=True)
    author = models.CharField(max_length=100)
    title = models.CharField(max_length=100)
    language = models.CharField(max_length=100)
    cover = models.CharField(max_length=200)

class Volume(models.Model):
    ebook_id = models.ForeignKey(Book)
    volume_number = models.IntegerField(default=0)
    text_file = models.CharField(max_length=200)
