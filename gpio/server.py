from flask import Flask, request

post_freq = "NULL"

app = Flask(__name__)

@app.route('/submit-frequeny', methods=['POST'])
def result():
    json = request.get_json()
    print(json)
    post_freq = json['frequency'] 
    print("freq had been updated to: " + post_freq)

def start_server():
    app.run(host="localhost" ,debug=False, port=8090)
