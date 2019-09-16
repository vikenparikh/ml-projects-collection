package com.aws.masterapptier.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.services.sqs.model.Message;
import com.aws.masterapptier.iterfaces.SqsInterface;

public class SqsServiceImpl implements SqsService {

	@Autowired
	private SqsInterface sqsInterface;

	@Override
	public void deleteMessage(String queueName, Message message) {
		sqsInterface.deleteMessage(queueName, message);
	}

	@Override
	public Message receiveMessage(String queueName, Integer waitTime, Integer visibilityTimeout) {
		return sqsInterface.receiveMessage(queueName, waitTime, visibilityTimeout);
	}

	@Override
	public void sendMessage(String queueName, String messageBody, Integer delaySeconds) {
		sqsInterface.sendMessage(queueName, messageBody, delaySeconds);
	}

}
