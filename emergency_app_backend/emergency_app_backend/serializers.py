from emergency_app.models import Emergency,Category
from rest_framework import serializers

class EmergencySerializer(serializers.HyperlinkedModelSerializer):

    class Meta:
        model = Emergency
        fields = ('category','lat','lon')


class CategorySerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Category
        fields = ('name',)