from db import db 

class Action(db.Model):
	action_id = db.Column(db.BigInteger, primary_key = True)
	project_id = db.Column(db.BigInteger, db.ForeignKey('project.project_id'), nullable = False)
	action_name = db.Column(db.String(20), nullable = True)
	action_detail = db.Column(db.String(80), nullable = True)