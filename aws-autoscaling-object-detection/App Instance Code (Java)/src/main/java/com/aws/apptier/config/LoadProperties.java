package com.aws.apptier.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LoadProperties {

	public static String getProperty(String property) {

		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Properties properties = new Properties();
		try (InputStream resourceStream = loader.getResourceAsStream("application.properties")) {
			properties.load(resourceStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties.getProperty(property);
	}
}
