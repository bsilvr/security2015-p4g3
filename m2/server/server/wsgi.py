"""
WSGI config for server project.

It exposes the WSGI callable as a module-level variable named ``application``.

For more information on this file, see
https://docs.djangoproject.com/en/1.8/howto/deployment/wsgi/
"""
"""
import os
import sys

from django.core.wsgi import get_wsgi_application

sys.path = ["/var/www/server"] + sys.path

os.environ.setdefault("DJANGO_SETTINGS_MODULE", "server.settings")

application = get_wsgi_application()
"""




import os
import sys
import site

# Add the site-packages of the chosen virtualenv to work with
site.addsitedir('../../home/vagrant/.virtualenvs/webStoreServer/local/lib/python2.7/site-packages')

# Add the app's directory to the PYTHONPATH
sys.path.append('/var/www/server')
sys.path.append('/var/www/server/server')

os.environ['DJANGO_SETTINGS_MODULE'] = 'server.settings'

# Activate your virtual env
activate_env=os.path.expanduser("../../home/vagrant/.virtualenvs/webStoreServer/bin/activate_this.py")
execfile(activate_env, dict(__file__=activate_env))


from django.core.wsgi import get_wsgi_application
application = get_wsgi_application()