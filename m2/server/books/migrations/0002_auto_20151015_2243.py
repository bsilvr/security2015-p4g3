# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('books', '0001_initial'),
    ]

    operations = [
        migrations.CreateModel(
            name='Volume',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('text_file', models.CharField(max_length=200)),
            ],
        ),
        migrations.RemoveField(
            model_name='book',
            name='id',
        ),
        migrations.RemoveField(
            model_name='book',
            name='text_file',
        ),
        migrations.AlterField(
            model_name='book',
            name='ebook_id',
            field=models.IntegerField(serialize=False, primary_key=True),
        ),
        migrations.AddField(
            model_name='volume',
            name='ebook_id',
            field=models.ForeignKey(to='books.Book'),
        ),
    ]
