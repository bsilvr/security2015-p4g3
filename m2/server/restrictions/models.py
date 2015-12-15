from django.db import models


class Countries(models.Model):
    country = models.CharField(max_length=100)


class OperatingSystem(models.Model):
    so = models.CharField(max_length=100)