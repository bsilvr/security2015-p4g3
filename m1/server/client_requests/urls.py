"""homeserver URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/1.8/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  url(r'^$', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  url(r'^$', Home.as_view(), name='home')
Including another URLconf
    1. Add an import:  from blog import urls as blog_urls
    2. Add a URL to urlpatterns:  url(r'^blog/', include(blog_urls))
"""
from django.conf.urls import include, url, patterns
from django.contrib import admin
from client_requests import views

urlpatterns = [
	url(r'^read_book/$', views.read_book, name='read_book'),
	#url(r'^validate/$', views.validate, name='validate'),
	url(r'^decrypt/$', views.decrypt, name='decrypt'),
	url(r'^get_file/$', views.get_file, name='get_file'),





]
