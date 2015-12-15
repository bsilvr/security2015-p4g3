# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('books', '0003_volume_volume_number'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='volume',
            name='ebook_id',
        ),
        migrations.AddField(
            model_name='book',
            name='text_file',
            field=models.CharField(default='NoPath', max_length=300),
            preserve_default=False,
        ),
        migrations.DeleteModel(
            name='Volume',
        ),
    ]
