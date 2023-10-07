# # # # # # # # # # #
# voorbeeld van gpt #
# # # # # # # # # # #

import RPi.GPIO as GPIO
import time
import mariadb

# Initialize GPIO
GPIO.setmode(GPIO.BCM)
SENSOR_PIN = 17  # GPIO pin number

# Initialize MariaDB connection
conn = mariadb.connect(
    user="scr",
    password="u0u6YiSVp9T2jQ1YsQ24bPPj0XoPZ8ug",
    host="database",
    database="scr"
)
cursor = conn.cursor()

try:
    while True:
        # Read analog value (replace with ADC code if using ADC)
        analog_value = GPIO.input(SENSOR_PIN)
        # Convert analog value to temperature (implement your conversion formula)

        # Insert data into database
        cursor.execute("INSERT INTO temperature_data (timestamp, temperature) VALUES (NOW(), %s)", (temperature,))
        conn.commit()

        # Wait for 100ms
        time.sleep(0.1)

except KeyboardInterrupt:
    GPIO.cleanup()
    conn.close()
