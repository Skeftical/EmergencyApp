from django.shortcuts import render

# Create your views here.

from django.http import HttpResponse
from rest_framework import viewsets
from models import Emergency, Category
from emergency_app_backend.serializers import EmergencySerializer, CategorySerializer
def index(request):
    context_dict ={}
    # Sends a set with all the suggested trips to the index template as part of the context dictionary
    # Return a rendered response to send to the client.
    # We make use of the shortcut function to make our lives easier.
    # Note that the first parameter is the template we wish to use.

    return render(request, 'index.html', context_dict)


def add_emergency(request):
    if request.method == 'POST':
        print(request)
    return HttpResponse("Hey")


class EmergencyViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows emergencies to be viewed or edited.
    """
    queryset = Emergency.objects.all()
    serializer_class = EmergencySerializer


class CategoryViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows emergencies to be viewed or edited.
    """
    queryset = Category.objects.all()
    serializer_class = CategorySerializer
