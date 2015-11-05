# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('users_data', '0003_remove_purchases_file_key'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='user_key',
            name='player_used',
        ),
    ]
