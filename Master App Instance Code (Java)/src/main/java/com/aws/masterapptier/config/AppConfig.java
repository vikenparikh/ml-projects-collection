package com.aws.masterapptier.config;

import org.springframework.context.annotation.Bean;

import com.aws.masterapptier.iterfaces.S3Interface;
import com.aws.masterapptier.iterfaces.S3InterfaceImpl;
import com.aws.masterapptier.iterfaces.SqsInterface;
import com.aws.masterapptier.iterfaces.SqsInterfaceImpl;
import com.aws.masterapptier.service.Listener;
import com.aws.masterapptier.service.ListenerImpl;
import com.aws.masterapptier.service.S3Service;
import com.aws.masterapptier.service.S3ServiceImpl;
import com.aws.masterapptier.service.SqsService;
import com.aws.masterapptier.service.SqsServiceImpl;

public class AppConfig {

	@Bean
	public Listener Listener() {
		return new ListenerImpl();
	}

	@Bean
	public AWSConfig awsConfig() {
		return new AWSConfig();
	}

	@Bean
	public SqsInterface sqsInterface() {
		return new SqsInterfaceImpl();
	}

	@Bean
	public SqsService sqsService() {
		return new SqsServiceImpl();
	}

	@Bean
	public S3Interface S3Interface() {
		return new S3InterfaceImpl();
	}

	@Bean
	public S3Service s3Service() {
		return new S3ServiceImpl();
	}

}
