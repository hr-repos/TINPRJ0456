#!/bin/bash

# Download the Java package
curl -o openjdk-21.tar.gz https://download.oracle.com/java/21/latest/jdk-21_linux-aarch64_bin.tar.gz

# Install Java
sudo mkdir -p /opt/jdk
sudo tar -zxf openjdk-21.tar.gz -C /opt/jdk
rm openjdk-21.tar.gz

# Set up alternatives
sudo update-alternatives --install /usr/bin/java java /opt/jdk/jdk-21/bin/java 21
sudo update-alternatives --install /usr/bin/javac javac /opt/jdk/jdk-21/bin/javac 21

# Check if Java is correctly installed
java -version
