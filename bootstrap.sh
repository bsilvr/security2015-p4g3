#!/usr/bin/env bash

# Install dependencies
apt-get update
apt-get install -y apache2 python-pip encfs

pip install Django==1.8.6 djangorestframework pycrypto
#########################################################

# Copy project folder to apache directory
cp -r /vagrant/m2/server /var/www/
#########################################################

# Create Secure File System
mkdir -p /opt/ebooks

ebook = "ebookwebstore"

encfs -S /opt/ebooks /var/www/server/media/books << EOF
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
###########################################################

# Copy apache config file
cp /vagrant/m2/server/configs/apache2/sites-enabled/default-ssl.conf /etc/apache2/sites-enabled/
############################################################

# Copy Certificates files
cp /vagrant/m2/server/certs/Server_CA.crt /etc/ssl/certs/
cp /vagrant/m2/server/certs/webStore.crt /etc/ssl/private/
cp /vagrant/m2/server/certs/webStoreKey.pem /etc/ssl/private/
############################################################

# Enable SSL
a2enmod ssl
service apache2 restart
#############################################################


