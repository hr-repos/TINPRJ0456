#!/bin/bash

sudo apt update
sudo apt upgrade

sudo apt remove apache2

sudo apt install -y debian-keyring debian-archive-keyring apt-transport-https
curl -1sLf 'https://dl.cloudsmith.io/public/caddy/stable/gpg.key' | sudo tee /etc/apt/trusted.gpg.d/caddy-stable.asc
curl -1sLf 'https://dl.cloudsmith.io/public/caddy/stable/debian.deb.txt' | sudo tee /etc/apt/sources.list.d/caddy-stable.list
sudo apt update
sudo apt install caddy

sudo mkdir /etc/caddy
sudo mkdir -p /var/www/html

# Check if Caddy is correctly installed
caddy -v

sudo mkdir -p /var/www/dashboard
