# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('player', '0001_initial'),
    ]

    operations = [
        migrations.AlterField(
            model_name='player',
            name='player_key',
            field=models.CharField(max_length=200),
        ),
        migrations.AlterField(
            model_name='player',
            name='signature',
            field=models.CharField(max_length=200),
        ),
    ]
