import tensorflow_hub as hub 
import numpy as np 
import time
import random
from functools import lru_cache
import tensorflow as tf
import json 
import os.path
from config import GOOGLE_EMBEDDINGS_TF_HUB_URL, FILE_FOLDER_PATH, DEFAULT_DATASET_PATH, DEFAULT_DATASET_NPY_PATH

with tf.device('/CPU:0'):
    embed = hub.load(GOOGLE_EMBEDDINGS_TF_HUB_URL)

d = dict()
nd = dict()

dataset_file = open(DEFAULT_DATASET_PATH)
dataset = json.load(dataset_file)['conversations']

print(len(dataset))


def default_dataset_answers(question):
    A = embed([question])[0]
    default_embeddings = np.load(DEFAULT_DATASET_NPY_PATH)
    ind = -1
    ans = 0
    print(default_embeddings.shape[0])
    for i in range(default_embeddings.shape[0]):
        B = default_embeddings[i]
        similarity = np.dot(A,B)/(np.linalg.norm(A)*np.linalg.norm(B))
        if similarity>ans and similarity>0.60:
            ind = i
            ans = similarity

    print(ind, dataset[ind][0])
    if ind==-1:
        return ""
    else:
        s = len(dataset[ind])
        rand_ind = 1
        return dataset[ind][rand_ind]



def getFile(path):
    if path in d:
        return d[path]
    else:
        if len(d)==64:
            keys = list(d.keys())
            for k in keys:
                d.pop(k)

        if not os.path.isfile(path):
            raise Exception(400, "Data file not found for given api.")
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

        if not os.path.isfile(path):
            raise Exception(400, "Data file not found for given api.")

        f = np.load(path)
        nd[path] = f
        return f

def saveEmbeddingsNumpy(path, name):
    try:
        data = getFile(path)
    except Exception as err:
        if(len(err.args)==2):
            status, msg = err.args
            raise Exception(status, msg)
        else:
            raise Exception(500, "Internal Server error")

    inp = []
    targ = []

    for d in data:
        inp.append(d['question'])
        targ.append(d['answer'])
    
    with tf.device('/CPU:0'):
        embeddings = np.array(embed(inp))
        np.save(FILE_FOLDER_PATH + name + ".npy", embeddings)


def findAnswer(question, path, np_path):
    with tf.device('/CPU:0'):
        sent_embd = np.array(embed([question])[0])
    try:
        embeddings = getNumpyFile(np_path)
        data = getFile(path)
    except Exception as err:
        if(len(err.args)==2):
            status, msg = err.args
            raise Exception(status, msg)
        else:
            raise Exception(500, "Internal Server error")

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
        return targ[ind], ind
    else:
        return "Sorry I didn't get that.", ind
        

def getAnswer(question, project_name):
    path = FILE_FOLDER_PATH + project_name + ".json"
    np_path = FILE_FOLDER_PATH + project_name + ".npy"
    try:
        answer, valid = findAnswer(question, path, np_path)
    except Exception as err:
        if(len(err.args)==2):
            status, msg = err.args
            raise Exception(status, msg)
        else:
            raise Exception(500, "Internal Server error")
    return answer, valid

def findEmbedding(project_name):
    name = project_name
    path = FILE_FOLDER_PATH + project_name + ".json"
    try:
        saveEmbeddingsNumpy(path, name)
    except Exception as err:
        if(len(err.args)==2):
            status, msg = err.args
            raise Exception(status, msg)
        else:
            raise Exception(500, "Internal Server error")
    
