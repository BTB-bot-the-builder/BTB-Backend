import tensorflow_hub as hub 
import numpy as np 
import time
import random
from functools import lru_cache

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

        f = open(path, 'r')
        d[path] = f.read()
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
    f = getFile(path)
    qa = f.split('\n')
    inp = []
    targ = []
    for x in qa:
        inp.append(x.split('\t')[0])
        targ.append(x.split('\t')[1])
    
    embeddings = np.array(embed(inp))
    np.save("D:\Minor-Project\\resources\\" + name + ".npy", embeddings)


def findAnswer(question, path, np_path):
    sent_embd = np.array(embed([question])[0])
    embeddings = getNumpyFile(np_path)
    f = getFile(path)
    data = f.split('\n')

    ind = -1
    ans = 0

    inp = []
    targ = []

    for x in data:
        inp.append(x.split('\t')[0])
        targ.append(x.split('\t')[1])

    for i in range(len(inp)):
        A = sent_embd
        B = embeddings[i]
        similarity = np.dot(A,B)/(np.linalg.norm(A)*np.linalg.norm(B))
        if similarity>ans and similarity>0.60:
            ind = i
            ans = similarity

    print("Dataset Sentence:", inp[ind])
    if(ind!=-1):
        print("Reply:", targ[ind])
    else:
        print("Reply: Sorry I didn't get that.")
        

saveEmbeddingsNumpy('D:\Minor-Project\\resources\dialogs.txt', 'diaglogs_numpy')

while True:
    ques = input("Enter question: ")
    findAnswer(ques, 'D:\Minor-Project\\resources\dialogs.txt', 'D:\Minor-Project\\resources\diaglogs_numpy.npy')