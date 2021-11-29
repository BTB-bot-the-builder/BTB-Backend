from flask import jsonify
from flask_restful import Resource, reqparse
from models.Project import Project 
import utils.WebSearch as WebSearch
import utils.Redirect as Redirect
import utils.Chatbot as ChatbotModel
import utils.IntentIdentifier as IntentIdentifier
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

		intent_type = IntentIdentifier.find(question, [a.action_name for a in project.actions])

		if intent_type == 0:
			answer = ChatbotModel.default_dataset_answers(question)
			if answer != "":
				return {
					"status": "200",
					"msg": "OK",
					"intent":"QA",
					"answer":answer
				}, 200


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
				links = [res[0]['link'], res[1]['link'], res[2]['link']]
				return {
					"intent":"web",
					'status':"200",
					"msg":"OK",
					"data":res,
					"title": "Amitabh Bachchan - 79 years",
					"link": "https://en.wikipedia.org/wiki/Amitabh_Bachchan"
				}, 200
			else:
				return {
					"intent":"QA",
			        "status": 200,
			        "msg": "OK",
			        "answer": answer
				}, 200
				
		elif intent_type == 1:

			question = Redirect.beautify(question)
			print(question)
			page_name = Redirect.get_page_name(question)
			
			if page_name.lower() in [a.action_name.lower() for a in project.actions]:

				link = [a.action_detail for a in project.actions if a.action_name.lower()==page_name.lower()][0]
				return {
					"intent":"redirect",
					'status': 200,
					'msg': 'OK',
					'webpage': page_name,
					'link':link
				}, 200
			else:
				return {
					"intent":"QA",
			        "status": 200,
			        "msg": "OK",
			        "answer": "I didn't get that."
				}, 200


class Feedback(Resource):

	parser = reqparse.RequestParser()

	parser.add_argument(
			'rating',
			type = int,
			required = True,
			help = 'A rating is required'
		)

	def post(self, project_id):
		data = self.parser.parse_args()
		rating = data['rating']

		project = Project.findById(project_id)

		if project is None:
			return {
					'status':"404",
					'msg':'Chatbot not found'
			}, 404

		project.incrementRatings() 

		return {
			'status':'200',
			'msg':'Rating recorded'
		}, 200





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

