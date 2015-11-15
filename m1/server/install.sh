#
#
# Script to install all needed libraries for execution
#
#
# usage: ./install.sh
printf "Intalling python-pip..."
sudo apt-get install python-pip
printf "done\n"

printf "Intalling Django..."
pip install Django==1.8.6
printf "done\n"

printf "Installing library dependencies..."
pip install pycrypto django-extensions Werkzeug pyOpenSSL
printf "done\n"

printf "Creating database..."
python manage.py migrate
printf "done\n"

printf "Populating database..."
python db_scripts.py
printf "done\n"
