from flask import Flask, request
import json

post_freq = 0

app = Flask(__name__)

@app.route('/submit-frequency', methods=['POST'])
def result():
    print("received submit-frequency")
    json = request.get_json()
    post_freq = int(json['frequency'])
    print("freq had been updated to: " + str(post_freq))
    return "201"

if __name__ == "__main__":
    app.run(host="0.0.0.0" ,debug=False, port=8090)
