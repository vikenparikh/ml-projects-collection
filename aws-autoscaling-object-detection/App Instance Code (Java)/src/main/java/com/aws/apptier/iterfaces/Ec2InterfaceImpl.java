package com.aws.apptier.iterfaces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.util.EC2MetadataUtils;
import com.aws.apptier.config.AWSConfig;

public class Ec2InterfaceImpl implements Ec2Interface {

	public static final Logger LOGGER = LoggerFactory.getLogger(Ec2InterfaceImpl.class);

	@Autowired
	private AWSConfig awsConfig;

	@Override
	public void endInstance() {
		String myId = EC2MetadataUtils.getInstanceId();
		StopInstancesRequest request = new StopInstancesRequest().withInstanceIds(myId);
		awsConfig.amazonEC2().stopInstances(request);
	}

}
