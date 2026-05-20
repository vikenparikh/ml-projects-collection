package com.aws.apptier.iterfaces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.aws.apptier.config.AWSConfig;
import com.aws.apptier.config.LoadProperties;

public class S3InterfaceImpl implements S3Interface {

	private static final Logger LOGGER = LoggerFactory.getLogger(S3InterfaceImpl.class);

	@Autowired
	private AWSConfig awsConfiguration;

	@Override
	public void insertObject(String key, String value) {
		LOGGER.debug("Inserting object into the bucket.");
		awsConfiguration.amazonS3().putObject(LoadProperties.getProperty("amazonProperties.bucketName"), key, value);
	}

}
