package com.aws.apptier.config;

import org.springframework.context.annotation.Bean;

import com.aws.apptier.iterfaces.Ec2Interface;
import com.aws.apptier.iterfaces.Ec2InterfaceImpl;
import com.aws.apptier.iterfaces.S3Interface;
import com.aws.apptier.iterfaces.S3InterfaceImpl;
import com.aws.apptier.iterfaces.SqsInterface;
import com.aws.apptier.iterfaces.SqsInterfaceImpl;
import com.aws.apptier.service.Ec2Service;
import com.aws.apptier.service.Ec2ServiceImpl;
import com.aws.apptier.service.Listener;
import com.aws.apptier.service.ListenerImpl;
import com.aws.apptier.service.S3Service;
import com.aws.apptier.service.S3ServiceImpl;
import com.aws.apptier.service.SqsService;
import com.aws.apptier.service.SqsServiceImpl;

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

	@Bean
	public Ec2Interface ec2Interface() {
		return new Ec2InterfaceImpl();
	}

	@Bean
	public Ec2Service ec2Service() {
		return new Ec2ServiceImpl();
	}

}
