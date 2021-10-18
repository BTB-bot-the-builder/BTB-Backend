import tensorflow_hub as hub 
import numpy as np 
import time
import random
from functools import lru_cache
import tensorflow as tf
import json 

embed = hub.load("https://tfhub.dev/google/universal-sentence-encoder/4")
d = dict()
nd = dict()

def getFile(path):
    if path in d:
        return d[path]
    else:
        if len(d)==64:
            keys = list(d.keys())
            for k in keys:
                d.pop(k)

        f = open(path)
        data = json.load(f)
        d[path] = data
        return d[path]

def getNumpyFile(path):
    if path in nd:
        return nd[path]
    else:
        if len(nd)==64:
            keys = list(nd.keys())
            for k in keys:
                nd.pop(k)
        f = np.load(path)
        nd[path] = f
        return f

def saveEmbeddingsNumpy(path, name):
    data = getFile(path)

    inp = []
    targ = []

    for d in data:
        inp.append(d['question'])
        targ.append(d['answer'])
    
    with tf.device('/CPU:0'):
        embeddings = np.array(embed(inp))
        np.save("D:\Minor-Project\\resources\\" + name + ".npy", embeddings)


def findAnswer(question, path, np_path):
    with tf.device('/CPU:0'):
        sent_embd = np.array(embed([question])[0])
    embeddings = getNumpyFile(np_path)
    data = getFile(path)

    ind = -1
    ans = 0

    inp = []
    targ = []

    for d in data:
        inp.append(d['question'])
        targ.append(d['answer'])

    for i in range(len(inp)):
        A = sent_embd
        B = embeddings[i]
        similarity = np.dot(A,B)/(np.linalg.norm(A)*np.linalg.norm(B))
        if similarity>ans and similarity>0.60:
            ind = i
            ans = similarity

    if(ind!=-1):
        return targ[ind]
    else:
        return "Sorry I didn't get that."
        

def getAnswer(question, project_name):
    path = "D:\Minor-Project\\resources\\" + project_name + ".json"
    np_path = "D:\Minor-Project\\resources\\" + project_name + ".npy"
    answer = findAnswer(question, path, np_path)
    return answer

def findEmbedding(project_name):
    name = project_name
    path = "D:\Minor-Project\\resources\\" + project_name + ".json"
    saveEmbeddingsNumpy(path, name)

# saveEmbeddingsNumpy('D:\Minor-Project\\resources\dialogs.txt', 'diaglogs_numpy')

# while True:
#     ques = input("Enter question: ")
#     findAnswer(ques, 'D:\Minor-Project\\resources\dialogs.txt', 'D:\Minor-Project\\resources\diaglogs_numpy.npy')

# findEmbedding("user-1-project-1")