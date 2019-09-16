package com.aws.apptier.service;

import com.amazonaws.services.sqs.model.Message;

public interface SqsService {

	void deleteMessage(String queueName, Message message);

	public Message receiveMessage(String queueName, Integer waitTime, Integer visibilityTimeout);

	public void sendMessage(String queueName, String messageBody, Integer delaySeconds);
}
