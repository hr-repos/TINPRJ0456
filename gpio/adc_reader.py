import time
import ADS1x15 
import RPi.GPIO as GPIO

#ads1/2 are the addr pins of the 2 adc's(analog to digital converter).
#you can switch which adc you wanna read,
#by setting the pin of the adc you wanna read to low and the othor high.
    
adc1 = ADS1x15.ADS1115(address = 0x48) #read adc 1
adc2 = ADS1x15.ADS1115(address = 0x49) #read adc 2

GAIN = 1
# gain: (gain is in what range the adc is tuned to measure)
#  - 2/3 = +/-6.144V
#  -   1 = +/-4.096V
#  -   2 = +/-2.048V
#  -   4 = +/-1.024V
#  -   8 = +/-0.512V
#  -  16 = +/-0.256V

def read_sensor_data():
    
    adc1_values = [0,0,0,0] # a0,a1,a2,a3
    adc2_values = [0,0,0,0] # a4,a5,a6,a7
    
    for i in range(4):
        adc1_values[i] = adc1.read_adc(i, gain=GAIN)
        
    for i in range(4):
        adc2_values[i] = adc2.read_adc(i, gain=GAIN)
    
    values : list = adc1_values + adc2_values
        
    return values


if __name__ == '__main__':  
    print('|  a0     |  a1     |  a2     |  a3     | a4      | a5      | a6      | a7      |')
    
    while True:
        values = read_sensor_data()
        print('| {0:>7} | {1:>7} | {2:>7} | {3:>7} | {4:>7} | {5:>7} | {6:>7} | {7:>7} |'.format(*values))
        time.sleep(0.5)
