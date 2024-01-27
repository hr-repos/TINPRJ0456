#!/bin/bash
cd ~
python3 -m venv myenv       # Create virtual environment
source myenv/bin/activate   # Activate virtual environment

# Install packages
pip install adafruit-ads1x15
pip install RPI.GPIO
pip3 insstall requests
pip3 install flask


