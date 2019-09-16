# -*- coding: utf-8 -*-
"""
Created on Fri Mar 1 02:27:43 2019

@author: Viken
"""

from flask import Flask,request,render_template,redirect,url_for
from flask_cors import CORS
import boto3
import uuid
from collections import defaultdict
import time
from threading import Thread,RLock
import sys

class p:
	def __init__(self):
		#Create a dictonary and initialize it
		self.global_dict=dict()
	def append(self,answer1):
		#use this to add a new response recived to the dictonary using it's unique id and answer response recieved.
		answer=answer1['Body'].split("+")
		if len(answer)==3:
			if str(answer[0]) not in self.global_dict:
				self.global_dict[str(answer[0])] = str(answer[1]+","+answer[2])
				#print("Yooooooq.global_dict",self.global_dict)
		else:
			if str(answer[0]) not in self.global_dict:
				self.global_dict[str(answer[0])]= str(answer[0])
				#print("Yooooooq.global_dict",self.global_dict)

app = Flask(__name__)

@app.route('/', methods=['GET','POST'])
def WebController():
	#print("app",app.config.get('q'))
	# Generate Unique id for each url request
	uniqueid = str(uuid.uuid4())
	# Send the unique id to the sqs queue after the request from user arrives
	response = sqs.send_message(QueueUrl=queue_url,	MessageAttributes={
		'JMS_SQS_DeduplicationId': {'DataType': 'String','StringValue':'11553145817087'},
		'JMSXGroupID':{'DataType': 'String','StringValue':'messageGroup1'},
		'JMS_SQSMessageType':{'DataType': 'String','StringValue':'text'},
		'documentType':{'DataType': 'String','StringValue':'java.lang.String'},},MessageBody=((uniqueid)))
	#print(response)
	#print("sent",uniqueid)
	while(True):
		#Make the server wait for 1 second and checking again until the request has returned the and the dictonary has the response of the unique id of the request
		time.sleep(1)
		#print("hi"+str(count))
		try:
			if str(uniqueid) in app.config.get('q').global_dict:
				#print("got it")
				#print(q.global_dict[str(uniqueid)])
				#Save the answer to myanswer variabe to return it to the user
				myanswer=app.config.get('q').global_dict[str(uniqueid)]
				# Delete the unique id and its result and return the result back to the user.
				del app.config.get('q').global_dict[str(uniqueid)]
				return myanswer
		except:
			#print("An exception occurred at Web Server")
			return "An exception occurred at Web Server"
			
class Dev(Thread):
	def __init__(self,q):
		#Create a thread class to pool the response from the sqs queue back 
		super(Dev, self).__init__()
		# q is the instance for the global class p which contains a global dictonary to add the received messages from the back queue
		#We assign the value for the Thread to be the same as the instance for the class p instance that is to be used for both send and recevied functionalities
		self.q = q
		
	def run(self):
		while(True):
			#print("poll",self.q)
			#Continuously poll the back queue if any new message has been added to the queue with intervals of 1 second.
			time.sleep(1)
			#We receive upto 10 messages at a time from the sqs back queue, and user 10 second long polling to reduce the number of requests to the sqs back queue
			response = sqs.receive_message(QueueUrl=queue_url_back,AttributeNames=['SentTimestamp'],MaxNumberOfMessages=10,MessageAttributeNames=['All'],WaitTimeSeconds=10)
			#print(response)
			if 'Messages' in response:
				for message in response['Messages']:
					self.q.append(message)
					if 'ReceiptHandle' in message:
						receipt_handle = message['ReceiptHandle']
						#print("receipt_handle",receipt_handle)
						#Delete the received messages which have been received and add it to the queue using the recipt handle of the message
						p=sqs.delete_message(QueueUrl=queue_url_back,ReceiptHandle=receipt_handle)
						#print("deleted"+str(answer[0]))
	
if __name__=='__main__':
	#initialize an instance for the global dictionary to add and delete the user requests and their corresponding responses
	i=p()
	#The url to send and receive the sqs messages from the sqs queues
	queue_url = 'https://sqs.us-west-1.amazonaws.com/372906587471/cloud_computing_queue'
	queue_url_back = 'https://sqs.us-west-1.amazonaws.com/372906587471/cloud_computing_queue_back'
	#Create a client for connecting the sqs queue
	sqs = boto3.client('sqs',region_name='us-west-1')
	#Create a Thread instance to poll the response back queue for responses
	t1=Dev(i)
	#Configure the flask web server to receive the responses to the same globally used dictonary 
	app.config['q']=t1.q
	CORS(app)
	#This would shut down the thread upon termination of the server 
	t1.daemon =True
	t1.start()
	#This is start the web server and start receiveing multiple requests by assigning multiple Worker threads for each requests
	app.run(host= '0.0.0.0',port=9000,threaded=True,debug=True,use_reloader=False)