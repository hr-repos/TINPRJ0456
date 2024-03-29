#!/bin/bash

exitting() {
    echo "Can't build. Exiting..."
    sleep 10
    exit 1
}

if ! type -p java || ! java -version 2>&1 | grep -q "version \"21"; then
    echo -e "\nJava 21 is not installed.\nInstallation: https://www.oracle.com/java/technologies/downloads/#jdk21-windows"
    exitting
fi

if ! type -p mvn; then
    echo -e "\nMaven is not installed.\nInstallation: https://phoenixnap.com/kb/install-maven-windows"
    exitting
fi

if ! type -p docker; then
    echo -e "\nDocker Desktop is not installed.\nInstallation: https://www.docker.com/products/docker-desktop/"
    exitting
fi

if ! type -p npm; then
    echo -e "\nNode (NPM) is not installed.\nInstallation: https://nodejs.org/en"
    exitting
fi

docker ps
if [ $? -ne 0 ]; then
    echo -e "\nDocker Desktop is not started up. Start it up first."
    exitting
fi

cd back-end
mvn clean package
docker build -t scr-api .

cd ../front-end
npm install
npm run build-only
docker build -t scr-proxy .

# cd ../gpio
# docker build -t scr-gpio .

cd ..
echo -e "\n\n\nStarting up Docker compose containers..."
docker-compose -p scr up -d
echo "Build process completed."
