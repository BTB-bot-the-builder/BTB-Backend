from flask import jsonify
from flask_restful import Resource, reqparse
from models.Project import Project 
import utils.WebSearch as WebSearch
import utils.Redirect as Redirect
import utils.Chatbot as ChatbotModel
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

		# chatbot
		try:
			answer = ChatbotModel.getAnswer(question, "user-"+str(user_id)+"-project-"+str(project_id))
		except Exception as err:
			status, msg = err.args
			return {
				"status": str(status),
				"msg": msg
			}, status
		return {
	        "status": 200,
	        "msg": "OK",
	        "answer": answer
		}, 200

		# redirect service
		# page_name = Redirect.get_page_name(question)

		# return {
		# 	'status': 200,
		# 	'msg': 'OK',
		# 	'webpage': page_name
		# }, 200

		#web search service
		# res = WebSearch.find_results(question)

		# return {
		# 	'status':"200",
		# 	"msg":"OK",
		# 	"data":res
		# }, 200

class Deploy(Resource):

	parser = reqparse.RequestParser()

	parser.add_argument('user',
			type = int,
			required = True,
			help = "user_id is required"
		)

	parser.add_argument('project',
			type = int,
			required = True,
			help = "project_id is required"
		)

	def post(self):

		data = self.parser.parse_args()
		user_id = data['user']
		project_id = data['project']

		try:
			ChatbotModel.findEmbedding( "user-"+str(user_id)+"-project-"+str(project_id))
		except Exception as err:
			status, msg = err.args
			return {
	            "status": str(status),
	            "msg": msg
			}, status
		return {
	        "status": 200,
	        "msg": "OK"
		}, 200

