from flask import Flask, request, jsonify
app = Flask(__name__)

@app.route('/hello', methods=['POST'])
def hello():
    content = request.json
    return jsonify({"message": "Hello, " + content["name"] + "!", "age": content["age"] * 2, "happy": not content["happy"]})

if __name__ == '__main__':
    app.run(host= '127.0.0.1', port=3316)