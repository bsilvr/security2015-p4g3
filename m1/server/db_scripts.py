from books.models import Book

b0 = Book(author="Elliot, Frances", title="Old Court Life in Spain", language="English", cover="http://i.imgur.com/DIvCaPV.jpg", ebook_id=49941)

b1 = Book(author="Wells, Carolyn", title="The Mystery of the Sycamore", language="English", cover="http://i.imgur.com/WdTJXit.jpg", ebook_id=50209)

b2 = Book(author="Bowen, Robert Sidney", title="Dave Dawson with the Pacific Fleet", language="English", cover="http://i.imgur.com/rnMUKIg.jpg", ebook_id=50217)

b3 = Book(author="Elliot, Frances", title="Old Court Life in France", language="English", cover="http://i.imgur.com/lnObH3e.jpg", ebook_id=50218)

b4 = Book(author="Various", title="The Railway Library, 1909", language="English", cover="http://imgur.com/sM6PJkM.jpg", ebook_id=50220)

b0.save()
b1.save()
b2.save()
b3.save()
b4.save()
