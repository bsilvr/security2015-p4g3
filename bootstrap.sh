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

cp /vagrant/m2/server/configs/.bashrc ~/

source ~/.bashrc

mkvirtualenv webStoreServer --no-site-packages

cd /var/www/server

workon webStoreServer

pip install -r requirements.txt

deactivate
#########################################################

# Create Secure File System
echo "Creating secure file system..."
mkdir -p /opt/ebooks

ebook = "ebookwebstore"

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
$ebook
$ebook
EOF

cp /vagrant/m2/server/media/books/* /var/www/server/media/books/
###########################################################

# Copy apache config file
echo "Copying apache file key..."
cp /vagrant/m2/server/configs/apache2/sites-enabled/default-ssl.conf /etc/apache2/sites-enabled/
############################################################

# Copy Certificates files
echo "Copying cert files..."
cp /vagrant/m2/server/certs/Server_CA.crt /etc/ssl/certs/
cp /vagrant/m2/server/certs/webStore.crt /etc/ssl/private/
cp /vagrant/m2/server/certs/webStoreKey.pem /etc/ssl/private/
############################################################

# Create database
echo "Creating database..."
rm /var/www/server/db.sqlite3
python /var/www/server/manage.py migrate
python /var/www/server/db_scripts.py

chown -R www-data /var/www/server/
chgrp -R www-data /var/www/server/

chown www-data /var/www/server/db.sqlite3
chgrp www-data /var/www/server/db.sqlite3

############################################################

# Enable SSL
a2enmod ssl
service apache2 restart
#############################################################


