from config import GOOGLE_EMBEDDINGS_TF_HUB_URL
import tensorflow as tf
import tensorflow_hub as hub 
import numpy as np

with tf.device('/CPU:0'):
    embed = hub.load(GOOGLE_EMBEDDINGS_TF_HUB_URL)

structures = [
	"Please take me to the <> page",
	"take me to <>",
	"redirect to <>",
	"open <> webpage",
	"open <>",
	"open <> page",
	"redirect to <> page",
	"take me to <> webpage",
	"take to <> page",
	"take to <>",
	"Please take me to the <> page",
	"take me to <> page",
	"redirect to <>",
	"open <> webpage",
	"open <>",
	"move to <>",
	"move to <> page",
	"please move to <> page",
	"please move to <>",
	"please open <>",
	"please open <> webpage"
	"please open <> page",
	"please move to <> section",
	"please open <> section",
	"open <> section",
	"take me to <> section",
	"take to <> section",
	"redirect to <> section",
	"please redirect to <> section",
	"Please take me to the <> section",
]

def find(question, action_names):
	sentences = []
	for name in action_names:
		for structure in structures:
			sentences.append(structure.replace("<>", name))
	query_embed = sent_embd = np.array(embed([question])[0])

	for sentence in sentences:
		embeddings = sent_embd = np.array(embed([sentence])[0])

		similarity = np.dot(query_embed,embeddings)/(np.linalg.norm(embeddings)*np.linalg.norm(query_embed))

		if similarity>0.60:
			return 1
	return 0
