from flask import Flask, request
from main import main, server
import json
from main import lock, post_freq

app = Flask(__name__)

@app.route('/submit-frequency', methods=['POST'])
def result():
    print("received submit-frequency")
    json = request.get_json()
    
    lock.acquire()
    post_freq = int(json['frequency'])
    lock.release()
    
    print("freq had been updated to: " + str(post_freq))
    return "201"

if __name__ == "__main__":
    app.run(host="0.0.0.0" ,debug=False, port=8090)
