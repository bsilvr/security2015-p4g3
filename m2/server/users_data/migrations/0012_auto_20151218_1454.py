# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('users_data', '0011_auto_20151218_1428'),
    ]

    operations = [
        migrations.AlterField(
            model_name='pteidlogin',
            name='random',
            field=models.CharField(max_length=200),
        ),
    ]
