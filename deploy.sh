#!/bin/bash

cd back-end

echo -e "\n\n\nBuilding Java API back-end..."
mvn clean package

if [ $? -ne 0 ]; then
    echo -e "\n\nMaven is not globally installed.\nBuild the back-end yourself with Maven.\n"
    read -p "Did you do this? (y/n): " choice

    if [ "$choice" != "y" ]; then
        echo "Can't build Docker images. Exiting..."
        sleep 5
        exit 1
    fi
fi

echo -e "\n\n\nBuilding Docker image for API..."
docker build -t scr-api .

cd ../front-end

echo -e "\n\n\nBuilding Vue front-end dashboard..."
npm install
npm run build-only

echo -e "\n\n\nBuilding Docker image for proxy, including dashboard..."
docker build -t scr-proxy .

cd ../gpio

echo -e "\n\n\nBuilding Docker image for GPIO..."
docker build -t scr-gpio .

cd ..


echo -e "\n\n\nStarting up Docker compose containers..."
docker-compose -p scr up -d


echo "Build process completed."
