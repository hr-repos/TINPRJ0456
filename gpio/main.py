from time import sleep
from adc_reader import read_sensor_data
from send_data_to_localhost import send_data, get_frequency
from server import post_freq, app
import RPi.GPIO as GPIO
import requests
import threading

def main_func():
    is_error_send = False
    
    try:
        sleep_time : int = get_frequency()
        print("get is : " + sleep_time)
    except:
        if not is_error_send:
            print("connection error failed to connect to localhost")
            is_error_send = True
        sleep_time = 1000

    while True:
        
        
        #sensor_data is an 8 element list with the 8 inputvalues of the ads1 abs2
        #where [ads1:a0, ads1:a1, ads1:a2, ads1:a3, ads2:a0, ads2:a1, ads2:a2, ads2:a3]
        
        #or in the code noted as:
        #    [a0,a1,a2,a3,a4,a5,a6,a7]
        try:
            sensors_data : list = read_sensor_data() 
        except:
            sensors_data : list = [0,0,0,0,0,0,0,0]
            print("could not read sensor data")
            continue
        
        try:
            send_data(sensors_data)
        except requests.ConnectionError:
            if not is_error_send:
                print("connection error failed to connect to localhost")
                is_error_send = True
            continue
        
        for i in range(0,8):
            data = sensors_data[i]
            print(i,": ", end="")
            print(data , end=", ")
        print()
        
            
        if(post_freq != 0):  #if a post is send with a frequency use that frequency
            sleep_time : int = post_freq
            
        if(sleep_time != 0): #if get_frequency didn't work  
            sleep(sleep_time / 1000)
            
server = threading.Thread(target=app.run(host="0.0.0.0" ,debug=False, port=8090))
main   = threading.Thread(target=main_func)
server.start()
main.start()
