package com.aws.scaleout.service;

public interface EC2Service {

	Integer getNumberOfInstances(String status);

	void createInstances(int count);

	void startInstances(int count);

}
