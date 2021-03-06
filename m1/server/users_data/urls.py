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
from django.conf.urls import url
from users_data import views

urlpatterns = [
	url(r'^create_user/$', views.create_user, name='create_user'),
    url(r'^login/$', views.user_login, name="login"),
    url(r'^logout/$', views.user_logout, name="logout"),
    url(r'^buy_book/$', views.buy_book, name="buy_book"),
    url(r'^get_purchases/$', views.get_purchases, name="get_purchases"),
    url(r'^register_device/$', views.register_device, name="register_device"),




]
