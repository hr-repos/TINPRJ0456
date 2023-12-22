#!/bin/sh

# rapberry instellen als hotspot

Sudo apt-get update

sudo cp /etc/wpa_supplicant/wpa_supplicant.conf /etc/wpa_supplicant/wpa_supplicant.conf.sav
sudo cp /dev/null /etc/wpa_supplicant/wpa_supplicant.conf

sudo echo 'ctrl_interface=DIR=/var/run/wpa_supplicant GROUP=netdev update_config=1' >> /etc/wpa_supplicant/wpa_supplicant.conf

# geef bij alle vragen die gesteld worden een ‘y’ behalve bij de vraag over wireguard, dit onderdeel is niet nodig
wget -q https://git.io/voEUQ -O /tmp/raspap && bash /tmp/raspap


# Als alles is goed gegaan zou je nu een wifi-netwerk moeten zien met de naam ‘raspi-webgui’. Op dit netwerk kan je inloggen met het wachtwoord ‘ChangeMe’. 
# Het ip-adres van de raspberry pi op dit netwerk is standaard 10.3.141.1
# Vervolgens kan je inloggen in de webinterface door naar het ip adres hierboven te gaan in de browser en met de inloggegevens ‘admin & secret’
# Hier kan je de naam en het wachtwoord van dit netwerk aanpassen en nog veel meer

# Je kan nu dus de GUI van je raspberry openen door verbinding te maken met het netwerk van je raspberry en in vnc verbinding te maken met het bijbehorende ip: 10.3.141.1. 
# Je hebt nu dus geen internet kabel meer nodig.
