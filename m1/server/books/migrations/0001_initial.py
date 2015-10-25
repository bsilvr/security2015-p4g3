# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Book',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('ebook_id', models.IntegerField(default=0)),
                ('author', models.CharField(max_length=100)),
                ('title', models.CharField(max_length=100)),
                ('language', models.CharField(max_length=100)),
                ('cover', models.CharField(max_length=200)),
                ('text_file', models.CharField(max_length=200)),
            ],
        ),
    ]
