package com.aws.apptier.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ObjectDetection {

	public static List<String> detectObject(String message) {

		List<String> output = new ArrayList<String>();
		try {
			ProcessBuilder pb = new ProcessBuilder("/home/ubuntu/darknet/detect_object.sh", "myArg1", "myArg2");
			Process p;
			p = pb.start();
			p.waitFor();

			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				output.add(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return output;

	}
}
