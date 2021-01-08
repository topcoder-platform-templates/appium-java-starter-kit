package com.environmentalSetup;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class EnviSetup {
	public static String rootDir;
	public static Properties configProp;

	/**
	 * reads the properties file (located at the root directory)   
	 */
	public static void readConfigProperties() { 
		rootDir = System.getProperty("user.dir");
		try {
			FileInputStream fs = new FileInputStream(new File(rootDir+"./Configuration.properties"));
			configProp = new Properties();
			configProp.load(fs);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
