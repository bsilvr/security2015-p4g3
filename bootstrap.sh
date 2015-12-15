#!/usr/bin/env bash

apt-get update
apt-get install -y apache2 python-pip

pip install Django==1.8.6 djangorestframework pycrypto

cp -r /vagrant/m2/server /var/www/

cp /vagrant/m2/server/configs/apache2/sites-enabled/server.conf /etc/apache2/sites-enabled/
cp /vagrant/m2/server/configs/apache2/sites-enabled/default-ssl.conf /etc/apache2/sites-enabled/

cp /vagrant/m2/server/certs/Server_CA.crt /etc/ssl/certs/
cp /vagrant/m2/server/certs/webStore.crt /etc/ssl/private/
cp /vagrant/m2/server/certs/webStoreKey.pem /etc/ssl/private/


a2enmod ssl

service apache2 restart
