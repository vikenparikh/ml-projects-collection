package com.aws.scaleout;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.aws.scaleout.config.AppConfig;
import com.aws.scaleout.service.ScaleOutService;

@SpringBootApplication
public class ScaleOutServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScaleOutServiceApplication.class, args);
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		ScaleOutService scaleOutService = context.getBean(ScaleOutService.class);
		scaleOutService.scaleOut();
		context.close();
	}

}
