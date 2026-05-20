package com.aws.apptier.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.aws.apptier.iterfaces.Ec2Interface;

public class Ec2ServiceImpl implements Ec2Service {

	@Autowired
	private Ec2Interface ec2Interface;

	@Override
	public void endInstance() {
		ec2Interface.endInstance();
	}

}
