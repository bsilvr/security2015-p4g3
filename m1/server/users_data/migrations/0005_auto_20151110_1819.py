# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('users_data', '0004_remove_user_key_player_used'),
    ]

    operations = [
        migrations.AlterField(
            model_name='devices',
            name='device_key',
            field=models.CharField(max_length=128),
        ),
        migrations.AlterField(
            model_name='purchases',
            name='random',
            field=models.CharField(max_length=128),
        ),
        migrations.AlterField(
            model_name='user_key',
            name='user_key',
            field=models.CharField(max_length=128),
        ),
    ]
