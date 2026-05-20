package com.aws.scaleout.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aws.scaleout.service.EC2Service;
import com.aws.scaleout.service.EC2ServiceImpl;
import com.aws.scaleout.service.ScaleOutService;
import com.aws.scaleout.service.ScaleOutServiceImpl;
import com.aws.scaleout.service.SQSService;
import com.aws.scaleout.service.SQSServiceImpl;

@Configuration
public class AppConfig {
	@Bean
	public SQSService sqsService() {
		return new SQSServiceImpl();
	}

	@Bean
	public EC2Service ec2Service() {
		return new EC2ServiceImpl();
	}

	@Bean
	public AWSConfig awsConfig() {
		return new AWSConfig();
	}

	@Bean
	public ScaleOutService loadBalancerService() {
		return new ScaleOutServiceImpl();
	}
}
