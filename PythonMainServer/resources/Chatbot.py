from flask_restful import Resource, reqparse
from models.Project import Project 
import py_eureka_client.eureka_client as eureka_client
import requests

class Info(Resource):

	parser = reqparse.RequestParser()

	parser.add_argument('api_key',
			type = str,
			required = True,
			help = "API Key is required"
		)

	def get(self, project_id):
		data = self.parser.parse_args()
		api_key = data['api_key']
		project = Project.findById(project_id)

		print(type(project.project_id))

		if project is None:
			return {
					'status': "404",
					"msg": "Chatbot not found"
			}, 404

		if api_key!=project.api_key:
			return {
					'status': "400",
					"msg": "Invalid api key"
			}, 400

		return {
				'status':200,
				'msg':'OK',
				'botName':project.bot_name,
				'description':project.description,
				'avatarUrl':project.avatar_url
		}, 200


class Chatbot(Resource):
	parser = reqparse.RequestParser()

	parser.add_argument('api_key',
			type = str,
			required = True,
			help = "API Key is required"
		)

	parser.add_argument('question',
			type = str,
			required = True,
			help = "Question missing"
		)

	def get(self, project_id):

		data = self.parser.parse_args()
		api_key = data['api_key']
		question = data['question']
		project = Project.findById(project_id)

		if project is None:
			return {
					'status':"404",
					'msg':'Chatbot not found'
			}, 404

		user_id = project.user_id
		project_id = project.project_id
		
		res = requests.get("http://localhost:8998/redirect?question="+question)
		return res.json(), 200



