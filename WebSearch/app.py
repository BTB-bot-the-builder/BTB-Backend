from flask import Flask, request, jsonify
import WebSearch
import py_eureka_client.eureka_client as eureka_client

rest_port = 8083
eureka_client.init(eureka_server="http://localhost:8761/eureka",
                   app_name="web-search",
                   instance_port=rest_port)

app = Flask(__name__)


@app.route('/websearch')
def get():
    query = request.args.get('q')
    return jsonify(WebSearch.find_results(query))

if __name__ == "__main__":
    app.run(host = "0.0.0.0", debug = True, port = 8083)