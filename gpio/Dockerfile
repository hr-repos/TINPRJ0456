FROM    python:3.8

RUN     pip install RPi.GPIO mariadb
WORKDIR /app
COPY    sensors.py .

CMD     python sensors.py
