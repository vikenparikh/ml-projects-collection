package com.aws.scaleout.service;

import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.aws.scaleout.config.AWSConfig;
import com.aws.scaleout.config.LoadProperties;

public class SQSServiceImpl implements SQSService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SQSService.class);

	@Autowired
	private AWSConfig awsConfig;

	@Override
	public Integer getNumberOfMsgs() {
		LOGGER.debug("Getting count of messages in the queue.");

		String queueUrl = awsConfig.amazonSQS().getQueueUrl(LoadProperties.getProperty("amazonProperties.requestQueue"))
				.getQueueUrl();
		
		ArrayList<String> attributeList = new ArrayList<String>();
		attributeList.add("ApproximateNumberOfMessages");

		Map<String, String> numOfMsgsMap = awsConfig.amazonSQS().getQueueAttributes(queueUrl, attributeList)
				.getAttributes();

		return Integer.valueOf((String) numOfMsgsMap.get("ApproximateNumberOfMessages"));
	}

}
