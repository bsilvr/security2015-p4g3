# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Player',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('player_key', models.BinaryField(max_length=200)),
                ('app_file', models.CharField(max_length=200)),
                ('signature', models.BinaryField(max_length=200)),
            ],
        ),
    ]
