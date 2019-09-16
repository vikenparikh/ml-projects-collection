package com.aws.scaleout.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.services.ec2.model.AmazonEC2Exception;
import com.amazonaws.services.ec2.model.DescribeInstanceStatusRequest;
import com.amazonaws.services.ec2.model.DescribeInstanceStatusResult;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceState;
import com.amazonaws.services.ec2.model.InstanceStateName;
import com.amazonaws.services.ec2.model.InstanceStatus;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StartInstancesResult;
import com.amazonaws.services.ec2.model.Tag;
import com.amazonaws.services.ec2.model.TagSpecification;
import com.aws.scaleout.config.AWSConfig;
import com.aws.scaleout.config.LoadProperties;

public class EC2ServiceImpl implements EC2Service {

	private static final Logger LOGGER = LoggerFactory.getLogger(EC2ServiceImpl.class);

	@Autowired
	private AWSConfig awsConfig;

	@Override
	public Integer getNumberOfInstances(String status) {
		DescribeInstanceStatusRequest describeRequest = new DescribeInstanceStatusRequest();
		describeRequest.setIncludeAllInstances(true);

		DescribeInstanceStatusResult describeInstancesResult = awsConfig.amazonEC2()
				.describeInstanceStatus(describeRequest);

		List<InstanceStatus> instanceStatusList = describeInstancesResult.getInstanceStatuses();

		Integer count = 0;

		for (InstanceStatus instanceStatus : instanceStatusList) {
			InstanceState instanceState = instanceStatus.getInstanceState();

			if (status.equalsIgnoreCase("Running")) {
				if (instanceState.getName().equals(InstanceStateName.Running.toString())
						|| instanceState.getName().equals(InstanceStateName.Pending.toString())) {
					count++;
				}
			} else {
				if (instanceState.getName().equals(InstanceStateName.Stopped.toString())
						|| instanceState.getName().equals(InstanceStateName.Stopping.toString())) {
					count++;
				}
			}

		}

		return count;
	}

	@Override
	public void createInstances(int count) {
		LOGGER.debug("Creating instances.");

		int minCount = count - 1;
		int maxCount = count;

		if (minCount == 0)
			minCount = 1;

		List<String> securityGroupId = new ArrayList<String>();
		securityGroupId.add(LoadProperties.getProperty("amazonProperties.securityGroupID"));

		Tag tag = new Tag();
		tag.setKey("Name");
		tag.setValue("Application-Instance");

		Collection<Tag> tags = new ArrayList<Tag>();
		tags.add(tag);

		TagSpecification tagSpecification = new TagSpecification();
		tagSpecification.setResourceType("instance");
		tagSpecification.setTags(tags);

		Collection<TagSpecification> tagSpecifications = new ArrayList<TagSpecification>();
		tagSpecifications.add(tagSpecification);

		RunInstancesRequest runInstanceRequest = new RunInstancesRequest();
		runInstanceRequest.setImageId(LoadProperties.getProperty("amazonProperties.appImageId"));
		runInstanceRequest.setInstanceType("t2.micro");
		runInstanceRequest.setSecurityGroupIds(securityGroupId);
		runInstanceRequest.setMinCount(minCount);
		runInstanceRequest.setMaxCount(maxCount);
		runInstanceRequest.setTagSpecifications(tagSpecifications);

		try {
			awsConfig.amazonEC2().runInstances(runInstanceRequest);
		} catch (AmazonEC2Exception amzEc2Exp) {
			LOGGER.debug("Error while creating instance. :" + amzEc2Exp.toString());
		} catch (Exception e) {
			LOGGER.debug("Error while creating instance. : " + e.toString());
		}

	}

	@Override
	public void startInstances(int count) {
		DescribeInstancesRequest request = new DescribeInstancesRequest();
		ArrayList<String> instanceID = new ArrayList<>();
		boolean done = false;
		while (!done) {
			DescribeInstancesResult response = awsConfig.amazonEC2().describeInstances(request);

			for (Reservation reservation : response.getReservations()) {
				for (Instance instance : reservation.getInstances()) {
					if (instance.getImageId().equals(LoadProperties.getProperty("amazonProperties.appImageId"))
							&& instance.getState().getName().equalsIgnoreCase("stopped") && count > 0) {
						instanceID.add(instance.getInstanceId());
						count--;
					}
					if (count == 0) {
						done = true;
					}
				}
			}
			request.setNextToken(response.getNextToken());

			if (response.getNextToken() == null) {
				done = true;
			}

		}

		StartInstancesRequest startRequest = new StartInstancesRequest(instanceID);
		awsConfig.amazonEC2().startInstances(startRequest);
	}

}
