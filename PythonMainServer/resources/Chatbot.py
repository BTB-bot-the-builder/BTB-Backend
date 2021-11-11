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
			help = "API key is required"
		)

	def get(self, project_id):
		data = self.parser.parse_args()
		api_key = data['api_key']
		try:
			project = Project.findById(project_id)
		except Exception as e:
			return {
				'status':"500",
				"msg":"Internal Server Error"
			}, 500

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
			help = "API key is required"
		)

	parser.add_argument('question',
			type = str,
			required = True,
			help = "Question is required"
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

		project.incrementRequests()

		# chatbot
		try:
			answer, valid = ChatbotModel.getAnswer(question, "user-"+str(user_id)+"-project-"+str(project_id))
		except Exception as err:
			status, msg = err.args
			return {
				"status": str(status),
				"msg": msg
			}, status

		if valid == -1:
			res = WebSearch.find_results(question)
			return {
				"intent":"web",
				'status':"200",
				"msg":"OK",
				"data":res
			}, 200
		else:
			return {
				"intent":"QA",
		        "status": 200,
		        "msg": "OK",
		        "answer": answer
			}, 200

		

		# redirect service
		# page_name = Redirect.get_page_name(question)

		# return {
		# 	"intent":"redirect",
		# 	'status': 200,
		# 	'msg': 'OK',
		# 	'webpage': page_name
		# }, 200


class Deploy(Resource):


	def post(self, project_id):

		# try:
		project = Project.findById(project_id)
		# except:
		# 	return {
		# 		"status":"500",
		# 		"msg":"Internal Server Error."
		# 	}, 500

		if project is None:
			return {
					'status':"404",
					'msg':'Chatbot not found'
			}, 404

		user_id = project.user_id

		project.setSecretKey()

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

