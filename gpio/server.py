from flask import Flask, request

post_freq = "NULL"

app = Flask(__name__)

app.run(debug=True, port=8090)

@app.route('/', methods=['submit-frequeny'])
def result():
    post_freq = request.form['frequency'] 
    print("freq had been updated to: " + post_freq)
    return 200 