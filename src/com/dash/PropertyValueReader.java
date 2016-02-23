package com.dash;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyValueReader {
	public String getPropertyValue()
	{
		String resultValue = "";
		InputStream inputStream = null;
		
		try {
			Properties prop = new Properties();
			String propFileName = "config.properties";
			File configFile = new File(propFileName);
			inputStream = new FileInputStream(configFile);
			
			prop.load(inputStream);
			inputStream.close();
 			// get the 'url' property value
			String url = prop.getProperty("url");
			String user = prop.getProperty("user");
			
			resultValue = url;
			System.out.println(resultValue + "\nProgram Ran on successfully by user = " + user);
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resultValue;
	}
}
