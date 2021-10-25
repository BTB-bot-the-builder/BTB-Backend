from flask import Flask, request, jsonify 
import Redirect 
import py_eureka_client.eureka_client as eureka_client

rest_port = 8998
eureka_client.init(eureka_server="http://localhost:8761/eureka",
                   app_name="redirect-service",
                   instance_port=rest_port)

app = Flask(__name__)

@app.route('/redirect')
def get_webpage_from_sentence():
	sentence = request.args.get('question')
	page_name = Redirect.get_page_name(sentence)
	return {
		'status': 200,
		'msg': 'Webpage extracted successfully',
		'webpage': page_name
	}, 200

if __name__ == "__main__":
	app.run(host = "0.0.0.0", debug = True, port = rest_port)	

