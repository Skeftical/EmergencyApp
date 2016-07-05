from django.conf.urls import patterns, url
from emergency_app import views
from django.conf.urls import url, include

urlpatterns = patterns('',
        url(r'^$', views.index, name='index'),
        url(r'add_emergency',views.add_emergency, name='emergency'),
        )