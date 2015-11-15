import os
os.environ.setdefault("DJANGO_SETTINGS_MODULE", "server.settings")

from books.models import Book
from restrictions.models import Countries, OperatingSystem
from player.models import Player


b0 = Book(author="Elliot, Frances", title="Old Court Life in Spain", language="English", cover="https://i.imgur.com/DIvCaPV.jpg", ebook_id=49941, text_file="media/books/pg49941.txt")

b1 = Book(author="Wells, Carolyn", title="The Mystery of the Sycamore", language="English", cover="https://i.imgur.com/WdTJXit.jpg", ebook_id=50209, text_file="media/books/pg50209.txt")

b2 = Book(author="Bowen, Robert Sidney", title="Dave Dawson with the Pacific Fleet", language="English", cover="https://i.imgur.com/rnMUKIg.jpg", ebook_id=50217, text_file="media/books/pg50217.txt")

b3 = Book(author="Elliot, Frances", title="Old Court Life in France", language="English", cover="https://i.imgur.com/lnObH3e.jpg", ebook_id=50218, text_file="media/books/pg50218.txt")

b4 = Book(author="Various", title="The Railway Library, 1909", language="English", cover="https://imgur.com/sM6PJkM.jpg", ebook_id=50220, text_file="media/books/pg50220.txt")

b0.save()
b1.save()
b2.save()
b3.save()
b4.save()


c1 = Countries(country="PT")

c1.save()


so1 = OperatingSystem(so="Mac OS X")
so2 = OperatingSystem(so="Windows 10")
so3 = OperatingSystem(so="Windows 8")
so4 = OperatingSystem(so="Windows 7")
so5 = OperatingSystem(so="Linux")

so1.save()
so2.save()
so3.save()
so4.save()
so5.save()

p0 = Player(player_key="kR5OjIhyGFLT7hSk", player_version="1.0.0")

p0.save()
