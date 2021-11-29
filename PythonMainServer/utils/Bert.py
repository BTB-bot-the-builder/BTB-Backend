from transformers import BertForQuestionAnswering, BertTokenizer, pipeline
import os.path as path
from bs4 import BeautifulSoup as bs
import requests

dir = 'deepset/bert-base-cased-squad2'


def getAnswers(question, urls):
    max_score = 0
    ans_r = ''
    status_r = 0
    contexts = []
    for url in urls:
        contexts += [getcontext(url)]
    model, tokenizer = load_model(dir)
    for context in contexts:
        answer, status, score = getAnswer(model, tokenizer, question, context)
        if status == 1 and score > max_score:
            ans_r = answer
            status_r = 1
            max_score = score
            print("Updating...",answer, max_score)
    return ans_r, status_r, max_score

def load_model(dir = dir):
    model = BertForQuestionAnswering.from_pretrained(dir)
    tokenizer = BertTokenizer.from_pretrained(dir)
    return model, tokenizer

def getcontext(url):
    response = requests.get(url= url)
    parsed = bs(response.content, "html.parser")
    elements = parsed.findAll("p")
    context = ''
    for i, element in enumerate(elements):
        context = context + ' ' + element.text
    if len(context) > 2500:
        context = context[:2499]
    return context


def getAnswer(model, tokenizer, question, context):
    predict = pipeline('question-answering', model = model, tokenizer = tokenizer)
    max_score = 0
    answer = ''
    end_point = 0
    if len(context) == 0:
        return ["Answer not found", 0, 0]
    print("Reached For loop")
    for i in range(5):
        if len(context)<=(i+1)*500:
            prediction = predict({
                    'context': context[i*500:len(context)],
                    'question': question
                })
            print(f"{i} prediction done, answer,score = ",prediction['answer'], prediction['score'])
            if prediction['score']>max_score:
                max_score = prediction['score']
                answer = prediction['answer']
            return [answer, 1, max_score]
        else:
            prediction = predict({
                'context': context[i*500:500*(i+1)],
                'question': question
            })
            print(f"{i} prediction done, answer,score =", prediction['answer'], prediction['score'])
        if prediction['score']>max_score:
            max_score = prediction['score']
            answer = prediction['answer']
            print("answer updated")

def getAnswer_mp(model, tokenizer, question, context):
    return 

# answer = getAnswers(
#     question = "where is taj mahal located?", 
#     urls = ["https://en.wikipedia.org/wiki/Taj_Mahal"])

# print(answer)