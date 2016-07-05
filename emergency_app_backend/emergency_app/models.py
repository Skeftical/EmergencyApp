from __future__ import unicode_literals

from django.db import models


class Category(models.Model):
    name = models.CharField(max_length=24, unique=True)
    def __unicode__(self):  #For Python 2, use __str__ on Python 3
        return self.name

# Create your models here.
class Emergency(models.Model):
    category = models.ForeignKey(Category, null=True)
    lat = models.DecimalField(max_digits=9, decimal_places=6,blank=True)
    lon = models.DecimalField(max_digits=9, decimal_places=6,blank=True)

    def __unicode__(self):  #For Python 2, use __str__ on Python 3
        return self.id