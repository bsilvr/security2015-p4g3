#!/usr/bin/env bash

# Install dependencies
echo "Install packages dependencies..."
apt-get update
apt-get install -y apache2 python-pip encfs libapache2-mod-wsgi python-dev build-essential

pip install virtualenvwrapper
########################################################

# Copy project folder to apache directory
echo "Coping project folder..."
cp -r /vagrant/m2/server /var/www/
#########################################################


# Setting Virtual Environment
echo "Setting up the Virtual Environment..."

export WORKON_HOME=/home/vagrant/.virtualenvs
source /usr/local/bin/virtualenvwrapper.sh

mkvirtualenv webStoreServer --no-site-packages

cd /var/www/server

workon webStoreServer

pip install -r requirements.txt

#########################################################

# Create database
echo "Creating database..."

DB=/var/www/server/db.sqlite3

if [ -f $DB ];
then
   echo "Database already exists, deleting it..."
   rm $DB
fi

python /var/www/server/manage.py migrate
python /var/www/server/db_scripts.py

chown -R www-data /var/www/server/
chgrp -R www-data /var/www/server/

chown www-data /var/www/server/db.sqlite3
chgrp www-data /var/www/server/db.sqlite3

# Deactivating virtualenv (if done earlier database would use system python)
deactivate
#########################################################

# Create Secure File System
echo "Creating secure file system..."
mkdir -p /opt/ebooks

EBOOK = "ebookwebstore"

encfs -S -o nonempty /opt/ebooks /var/www/server/media/books << EOF
x
1
256
1024
1
Yes
Yes
No
No
0
Yes
$EBOOK
$EBOOK
EOF

cp /vagrant/m2/server/media/books/* /var/www/server/media/books/
###########################################################

# Copy apache config file
echo "Copying apache config file..."
cp /vagrant/m2/server/configs/apache2/sites-enabled/default-ssl.conf /etc/apache2/sites-enabled/
############################################################

# Copy Certificates files
echo "Copying cert files..."
cp /vagrant/m2/server/certs/Server_CA.crt /etc/ssl/certs/
cp /vagrant/m2/server/certs/webStore.crt /etc/ssl/private/
cp /vagrant/m2/server/certs/webStoreKey.pem /etc/ssl/private/
############################################################

# Enable SSL
a2enmod ssl
service apache2 restart
#############################################################


