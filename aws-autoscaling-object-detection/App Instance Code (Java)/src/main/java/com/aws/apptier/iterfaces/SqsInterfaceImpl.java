package com.aws.apptier.iterfaces;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.QueueDoesNotExistException;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.aws.apptier.config.AWSConfig;

public class SqsInterfaceImpl implements SqsInterface {
	private static final Logger LOGGER = LoggerFactory.getLogger(SqsInterfaceImpl.class);

	@Autowired
	private AWSConfig awsConfig;

	@Override
	public void deleteMessage(String queueName, Message message) {
		LOGGER.debug("Deleting message");
		String queueUrl = awsConfig.amazonSQS().getQueueUrl(queueName).getQueueUrl();
		DeleteMessageRequest deleteMessageRequest = new DeleteMessageRequest(queueUrl, message.getReceiptHandle());
		awsConfig.amazonSQS().deleteMessage(deleteMessageRequest);

	}

	@Override
	public Message receiveMessage(String queueName, Integer waitTime, Integer visibilityTimeout) {
		LOGGER.debug("Receiving the message from the queue.");
		String queueUrl = awsConfig.amazonSQS().getQueueUrl(queueName).getQueueUrl();
		ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
		receiveMessageRequest.setMaxNumberOfMessages(1);
		receiveMessageRequest.setWaitTimeSeconds(waitTime);
		receiveMessageRequest.setVisibilityTimeout(visibilityTimeout);
		ReceiveMessageResult receiveMessageResult = awsConfig.amazonSQS().receiveMessage(receiveMessageRequest);
		List<Message> messageList = receiveMessageResult.getMessages();
		if (messageList.isEmpty()) {
			return null;
		}
		return messageList.get(0);
	}

	@Override
	public void sendMessage(String queueName, String messageBody, Integer delaySeconds) {
		LOGGER.debug("Sending the message into the queue.");
		String queueUrl = awsConfig.amazonSQS().getQueueUrl(queueName).getQueueUrl();
		queueUrl = awsConfig.amazonSQS().getQueueUrl(queueName).getQueueUrl();
		SendMessageRequest sendMessageRequest = new SendMessageRequest().withQueueUrl(queueUrl)
				.withMessageBody(messageBody).withDelaySeconds(delaySeconds);
		awsConfig.amazonSQS().sendMessage(sendMessageRequest);

	}

}