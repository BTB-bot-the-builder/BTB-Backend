import random

class Generator:

	@classmethod
	def generate(cls):

		options = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123654789@$%^&*"
		
		key = "BTB"
		for i in range(47):
			n = random.randint(0,len(options)-1)
			key+=options[n]
		return key
