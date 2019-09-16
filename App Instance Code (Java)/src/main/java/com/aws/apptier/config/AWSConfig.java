
package com.aws.apptier.config;

import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

@Configuration
public class AWSConfig {

	public BasicAWSCredentials basicAWSCredentials() {
		return new BasicAWSCredentials(LoadProperties.getProperty("amazonProperties.accessKey"),
				LoadProperties.getProperty("amazonProperties.secretKey"));
	}

	public AmazonEC2 amazonEC2() {
		AmazonEC2 amazonEC2 = AmazonEC2ClientBuilder.standard()
				.withRegion(LoadProperties.getProperty("amazonProperties.region"))
				.withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials())).build();
		return amazonEC2;
	}

	public AmazonSQS amazonSQS() {
		AmazonSQS amazonSQSClient = AmazonSQSClientBuilder.standard()
				.withRegion(LoadProperties.getProperty("amazonProperties.region"))
				.withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials())).build();
		return amazonSQSClient;
	}

	public AmazonS3 amazonS3() {
		AmazonS3 amazonS3Client = AmazonS3ClientBuilder.standard()
				.withRegion(LoadProperties.getProperty("amazonProperties.region"))
				.withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials())).build();
		return amazonS3Client;
	}

}