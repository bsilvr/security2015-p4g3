#
#
# Script to install all needed libraries for execution
#
#
# usage: ./install.sh
printf "Intalling python-pip...\n"
sudo apt-get install python-pip
printf "done\n"

printf "Intalling Django...\n"
pip install Django==1.8.6
printf "done\n"

printf "Installing library dependencies...\n"
pip install pycrypto django-extensions Werkzeug pyOpenSSL djangorestframework
printf "done\n"

printf "Removing previous databases...\n"
rm db.sqlite3
printf "done\n"

printf "Creating database...\n"
python manage.py migrate
printf "done\n"

printf "Populating database...\n"
python db_scripts.py
printf "done\n"
