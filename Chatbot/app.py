import Chatbot 
from flask import Flask, request, jsonify
import py_eureka_client.eureka_client as eureka_client

rest_port = 8084
# eureka_client.init(eureka_server="http://localhost:8761/eureka",
#                    app_name="chatbot",
#                    instance_port=rest_port)

app = Flask(__name__)

@app.route('/chatbot')
def getAnswers():
    question=request.args.get('question')
    project = request.args.get('project')
    user = request.args.get('user')
    answer = Chatbot.getAnswer(question, "user-"+user+"-project-"+project)
    return {
        "status": 200,
        "msg": "OK",
        "answer": answer
    }


@app.route("/embeddings/find", methods = ['POST'])
def findEmbeddings():
    project = request.args.get('project')
    user = request.args.get('user')
    print("here")
    answer = Chatbot.findEmbedding("user-"+user+"-project-"+project)
    return {
        "status": 200,
        "msg": "OK"
    }
    
if __name__ == "__main__":
    app.run(host = "0.0.0.0", debug = True, port = 8084)
    
