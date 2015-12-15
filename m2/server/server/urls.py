"""server URL Configuration

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
from django.conf.urls import include, url
from django.contrib import admin
from server import views, settings
from books import urls as books_urls
from users_data import urls as users_urls
from client_requests import urls as client_requests_urls


urlpatterns = [
    url(r'^$', views.home, name='server'),
    url(r'^register/$', views.register_page, name='register'),


    url(r'^books/', include(books_urls)),
    url(r'^users/', include(users_urls)),
    url(r'^requests/', include(client_requests_urls)),





    url(r'^admin/', include(admin.site.urls)),
    #url(r'^media/(?P<path>.*)$', 'django.views.static.serve',
    #    {'document_root': settings.MEDIA_ROOT}),
]
