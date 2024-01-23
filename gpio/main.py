from time import sleep
from adc_reader import read_sensor_data
from send_data_to_localhost import send_data, get_frequency
from server import post_freq
import RPi.GPIO as GPIO
import requests

sleep_time = get_frequency()

while True:
    #sensor_data is an 8 element list with the 8 inputvalues of the ads1 abs2
    #where [ads1:a0, ads1:a1, ads1:a2, ads1:a3, ads2:a0, ads2:a1, ads2:a2, ads2:a3]
    
    #or in the code noted as:
    #    [a0,a1,a2,a3,a4,a5,a6,a7]
    sensors_data : list = read_sensor_data() 
    
    try:
        send_data(sensors_data)
    except requests.ConnectionError:
        print("connection error failed to connect to localhost")
        continue
    
    for i in range(0,8):
        data = sensors_data[i]
        print(i,": ", end="")
        print(data , end=", ")
    print()
    
        
    if(post_freq != "NULL"):
        sleep_time = post_freq
        
    sleep(sleep_time / 1000)