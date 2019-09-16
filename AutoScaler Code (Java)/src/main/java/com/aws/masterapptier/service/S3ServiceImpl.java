package com.aws.masterapptier.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.aws.masterapptier.iterfaces.S3Interface;

public class S3ServiceImpl implements S3Service {
	@Autowired
	private S3Interface s3Interface;

	@Override
	public void insertObject(String key, String value) {
		s3Interface.insertObject(key, value);
	}
}
