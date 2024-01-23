from time import sleep
import requests
import json
from random import randint

URL_post    = 'http://localhost:8080/api/sensors'
URL_get     = 'http://localhost:8080/get-frequency'

def send_data(sensor_list : list):
    data = {"data": sensor_list}
    json_data = json.dumps(data)
    
    response = requests.post(URL_post, json_data)

    if response.status_code != 200:
        print(f'Failed to send data: {response.status_code} - {response.text}')
        
    return response.status_code

def get_frequency():
    get_json = requests.get(URL_get).json()
    return get_json['frequency']
        
if __name__ == '__main__':
    while True:
        foo_list = [0,1,2,3,4,5,6,7]
        status = ""
        
        #for i in range(0,8):
            #foo_list.append(randint(200, 250))
        try:
            status = send_data(foo_list)
        except requests.ConnectionError:
            print("conection error")
            continue
        
        print(status)
        
        sleep(0.1)
   