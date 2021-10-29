from flask import Flask
from flask_restful import Api
from resources.Chatbot import Info, Chatbot
import py_eureka_client.eureka_client as eureka_client
from models.Project import Project
import requests

rest_port = 8990
app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql://root:dakshdaksh@localhost/minorprojectdemo2'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

eureka_client.init(eureka_server="http://localhost:8761/",
                   app_name="python-main-server",
                   instance_port=rest_port)

api = Api(app)


api.add_resource(Info, '/api/chatbot/<int:project_id>/info')
api.add_resource(Chatbot, '/api/chatbot/<int:project_id>/chat')


if __name__ == "__main__":
	from db import db
	db.init_app(app)
	app.run(debug = True, port = rest_port)