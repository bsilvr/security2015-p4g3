# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('users_data', '0002_auto_20151029_1117'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='purchases',
            name='file_key',
        ),
    ]
