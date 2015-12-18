# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('users_data', '0007_auto_20151218_1419'),
    ]

    operations = [
        migrations.AlterField(
            model_name='user_key',
            name='public_key',
            field=models.CharField(default=None, max_length=400, blank=True),
        ),
    ]
