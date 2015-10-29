from django.shortcuts import render_to_response, render
from django.http import HttpResponse
from django.template import Template, Context,RequestContext
from books.models import Book, Volume

def home(request):
	template = "index.html"

	book_list = Book.objects.all()

	context = RequestContext(request, {
		'book_list': book_list,
	})

	return render(request, template, context)


def register_page(request):
    template = 'registration.html'

    context = {}

    return render(request, template, context)
