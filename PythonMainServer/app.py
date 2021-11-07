from flask import Flask
from flask_restful import Api
from resources.Chatbot import Info, Chatbot, Deploy
import py_eureka_client.eureka_client as eureka_client
from models.Project import Project
from config import DB_URL, REST_PORT, EUREKA_SERVER_URL, APP_NAME

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = DB_URL
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

eureka_client.init(eureka_server=EUREKA_SERVER_URL,
                   app_name=APP_NAME,
                   instance_port=REST_PORT)

api = Api(app)


api.add_resource(Info, '/api/chatbot/<int:project_id>/info')
api.add_resource(Chatbot, '/api/chatbot/<int:project_id>/chat')
api.add_resource(Deploy, '/chatbot/<int:project_id>')


if __name__ == "__main__":
	from db import db
	db.init_app(app)
	app.run(debug = True, port = REST_PORT)