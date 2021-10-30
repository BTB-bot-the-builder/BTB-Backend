from db import db 
from models.Action import Action

class Project(db.Model):
	project_id = db.Column(db.BigInteger, primary_key = True)
	project_name = db.Column(db.String(20), nullable = False)
	bot_name = db.Column(db.String(20))
	description = db.Column(db.String(20))
	avatar_url = db.Column(db.String(80))
	api = db.Column(db.String(80), unique = True)
	api_key = db.Column(db.String(80))
	model_path = db.Column(db.String(80))
	data_file_path = db.Column(db.String(80))
	state = db.Column(db.Integer)
	total_requests = db.Column(db.Integer)
	total_ratings = db.Column(db.Integer)
	sum_ratings = db.Column(db.Integer)
	user_id = db.Column(db.BigInteger)
	actions = db.relationship('Action', backref = 'project', lazy = True)

	@classmethod
	def findById(cls, project_id):
		return Project.query.get(project_id)

	def incrementRequests(self):
		self.total_requests += 1
		db.session.commit()


