package com.aws.scaleout.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ScaleOutServiceImpl implements ScaleOutService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScaleOutServiceImpl.class);
	@Autowired
	private SQSService sqsService;

	@Autowired
	private EC2Service ec2Service;

	@Override
	public void scaleOut() {
		while (true) {
			Integer numberOfMsgs = sqsService.getNumberOfMsgs();
			
			LOGGER.debug("Number of messages in queue : " + numberOfMsgs);
			
			Integer numOfRunningAppInstances = ec2Service.getNumberOfInstances("Running") - 1;
			
			Integer numofStoppedAppInstances = ec2Service.getNumberOfInstances("Stopped");
			
			Integer numOfAppInstances = numOfRunningAppInstances + numofStoppedAppInstances;
			
			LOGGER.debug("Number of Running App Instances : " + numOfRunningAppInstances);
			LOGGER.debug("Number of Stopped App Instances : " + numofStoppedAppInstances);
			LOGGER.debug("Number of App Instances : " + numOfAppInstances);
			

			if (numberOfMsgs > 0 && numberOfMsgs > numOfRunningAppInstances) {
				Integer count = Math.min(19 - numOfAppInstances, numberOfMsgs - numOfRunningAppInstances);
				
				if (count > 0 && numofStoppedAppInstances > 0) {
					LOGGER.debug("Number of App Instances to start : " + Math.min(count,numofStoppedAppInstances));
					ec2Service.startInstances(Math.min(count,numofStoppedAppInstances));
					count -= numofStoppedAppInstances;
				}
				
				if (count > 0) {
					LOGGER.debug("Number of App Instances to create : " + count);
					ec2Service.createInstances(count);
				}
					
			}
			LOGGER.debug("-------------------");
			try {
				Thread.sleep(40000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

}
