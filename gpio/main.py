from time import sleep
from adc_reader import read_sensor_data
from send_data_to_localhost import send_data, get_frequency
from server import post_freq, app
import RPi.GPIO as GPIO
import requests
import threading

lock = threading.Lock()

def run_server():
    app.run(host="0.0.0.0" ,debug=False, port=8090)

def try_get_freq():
    sleep_time : int = 1000
    
    print("Trying to retrieve frequency from backend")
    try:
        sleep_time : int = get_frequency()
        print("Received frequency is: " + str(sleep_time) + "ms")
    except:
        print("Connection error: failed to connect to localhost")
            
    return sleep_time

def main_func(freq : int):
    sens_error_send = False
    is_error_send = False
    sleep_time = freq
    
    print("Starting measurements")
    while True:

        #sensor_data is an 8 element list with the 8 inputvalues of the ads1 abs2
        #where [ads1:a0, ads1:a1, ads1:a2, ads1:a3, ads2:a0, ads2:a1, ads2:a2, ads2:a3]
        
        #or in the code noted as:
        #    [a0,a1,a2,a3,a4,a5,a6,a7]
        try:
            sensors_data : list = read_sensor_data() 
            sens_error_send = False
        except:
            sensors_data : list = [0,0,0,0,0,0,0,0]
            if not sens_error_send:
                print("could not read sensor data")
                sens_error_send = True
        
        try:
            send_data(sensors_data)
            is_error_send = False
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

freq : int = try_get_freq()
server = threading.Thread(target=run_server)
main   = threading.Thread(target=main_func, args=(freq,))
server.start()
main.start()

server.join()
main.join()
