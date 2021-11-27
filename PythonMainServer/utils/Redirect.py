import numpy as np 
import tensorflow as tf 

#tensorflow dependencies
from tensorflow.keras.preprocessing.sequence import pad_sequences
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Embedding
from tensorflow.keras.layers import Dense, Input
from tensorflow.keras.layers import TimeDistributed
from tensorflow.keras.layers import LSTM, Bidirectional
from tensorflow.keras.models import Model
from tensorflow.keras.preprocessing.text import Tokenizer
from tensorflow.keras.preprocessing.text import tokenizer_from_json
from tensorflow.keras.preprocessing.sequence import pad_sequences
from config import MODEL_PATH, TOKENIZER_PATH

def load_tokenizer(path):
	with open(path, "r") as f:
		json = f.read()
  
	return tokenizer_from_json(json)


tokenizer = load_tokenizer(TOKENIZER_PATH)

with tf.device('/CPU:0'):
	lstm = tf.keras.models.load_model(MODEL_PATH, compile = False)

def create_padded_sequences(sentences, tokenizer):
	'''
	convert the sentence in a numeric sequence with proper padding
	'''
	with tf.device('/CPU:0'):
		sequences = tokenizer.texts_to_sequences(sentences)
		padded = pad_sequences(sequences, padding = 'post', maxlen = 15)
	return np.array(padded)

def get_page_name(test_sentence):
	test_padded = create_padded_sequences([test_sentence], tokenizer)
	with tf.device('/CPU:0'):
		test_output = lstm.predict(test_padded)
	return test_sentence.split(" ")[np.argmax(test_output)]

def beautify(question):
 
	# initializing punctuations string
	punc = '''!()-[]{};:'"\\,<>./?@#$%^&*_~'''
	 
	# Removing punctuations in string
	# Using loop + punctuation string
	for ele in question:
	    if ele in punc:
	        question = question.replace(ele, "")

	q = ""
	l = question.split(' ')
	for word in l:
		if len(word)>0:
			q = q+word+' '
	return q[:-1]

# sentence = input("enter the command: ")
# print(get_page_name(sentence))