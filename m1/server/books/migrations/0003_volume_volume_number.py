# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('books', '0002_auto_20151015_2243'),
    ]

    operations = [
        migrations.AddField(
            model_name='volume',
            name='volume_number',
            field=models.IntegerField(default=0),
        ),
    ]
