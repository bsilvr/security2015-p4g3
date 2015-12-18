# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models
from django.conf import settings


class Migration(migrations.Migration):

    dependencies = [
        migrations.swappable_dependency(settings.AUTH_USER_MODEL),
        ('users_data', '0005_auto_20151110_1819'),
    ]

    operations = [
        migrations.CreateModel(
            name='PteIdLogin',
            fields=[
                ('transactionId', models.AutoField(serialize=False, primary_key=True)),
                ('random', models.CharField(max_length=128)),
                ('user', models.ForeignKey(to=settings.AUTH_USER_MODEL)),
            ],
        ),
        migrations.AddField(
            model_name='user_key',
            name='public_key',
            field=models.CharField(default=None, max_length=400),
        ),
    ]
