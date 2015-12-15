# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('player', '0002_auto_20151110_1819'),
    ]

    operations = [
        migrations.AddField(
            model_name='player',
            name='player_version',
            field=models.CharField(default='1.0.0', unique=True, max_length=50),
            preserve_default=False,
        ),
    ]
