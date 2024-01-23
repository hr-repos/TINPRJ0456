from time import sleep
from adc_reader import read_sensor_data
from send_data_to_localhost import send_data, get_frequency
from server import post_freq, start_server
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
        sleep_time = "NULL"

    while True:
        #sensor_data is an 8 element list with the 8 inputvalues of the ads1 abs2
        #where [ads1:a0, ads1:a1, ads1:a2, ads1:a3, ads2:a0, ads2:a1, ads2:a2, ads2:a3]
        
        #or in the code noted as:
        #    [a0,a1,a2,a3,a4,a5,a6,a7]
        sensors_data : list = [0,1,2,3,4,5,6,7]#read_sensor_data() 
        
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
        
            
        if(post_freq != "NULL"):  #if a post is send with a frequency use that frequency
            sleep_time : int = post_freq
            
        if(sleep_time != "NULL"): #if get_frequency didn't work  
            sleep(1)#(sleep_time / 1000)
            
server = threading.Thread(target=start_server)
main   = threading.Thread(target=main_func)
server.start()
main.start()
