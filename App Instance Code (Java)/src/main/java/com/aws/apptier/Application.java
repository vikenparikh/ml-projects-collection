package com.aws.apptier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.aws.apptier.config.AppConfig;
import com.aws.apptier.service.Listener;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		Listener listenerService = context.getBean(Listener.class);
		listenerService.main();
		context.close();
	}

}