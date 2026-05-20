package com.aws.masterapptier.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.services.sqs.model.Message;
import com.aws.masterapptier.config.LoadProperties;

public class ListenerImpl implements Listener {

	private static final Logger LOGGER = LoggerFactory.getLogger(ListenerImpl.class);

	@Autowired
	private SqsService sqsService;

	@Autowired
	private S3Service s3Service;

	@Override
	public void main() {

		LOGGER.debug("Starting Application Instance");
		while (true) {
			Message message = sqsService.receiveMessage(LoadProperties.getProperty("amazonProperties.requestQueue"), 10,
					60);
			if (message == null) {
				continue;
			}
			LOGGER.debug("Message ID: " + message.getBody());
			List<String> prediction = ObjectDetection.detectObject(message.getBody());
			LOGGER.debug("Prediction : " + prediction);

			s3Service.insertObject(prediction.get(0), prediction.get(1));
			sqsService.sendMessage(LoadProperties.getProperty("amazonProperties.responseQueue"),
					message.getBody() + "+" + prediction.get(0) + "+" + prediction.get(1), 0);
			sqsService.deleteMessage(LoadProperties.getProperty("amazonProperties.requestQueue"), message);
		}

	}

}
