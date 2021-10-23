import numpy as np

with open("D:\\Minor-Project\\resources\\train.txt", "r") as f:
	sentences = f.read().split("\n\n")
	lines = []
	tag_lines = []
	for s in sentences:
		line = ''
		tag_line = ''
		for x in s.split('\n'):
			ind = x.find(' ')
			line+=x[:ind] + " "
			ind2 = x[ind+1:].find(' ')
			tag_line += x[ind+1:ind2+ind+1] + " "
		line+='\n'
		tag_line+='\n'
		lines.append(line)
		tag_lines.append(tag_line)

# print(tag_lines[:10])

with open("D:\\Minor-Project\\resources\\sentences.txt", "w") as f:
	f.writelines(lines)

with open("D:\\Minor-Project\\resources\\tags.txt", "w") as f:
	f.writelines(tag_lines)