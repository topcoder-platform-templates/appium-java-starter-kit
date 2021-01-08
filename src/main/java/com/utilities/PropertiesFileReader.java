package com.utilities;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import com.baseRepo.Configuration;

/**
 * used when the Configuration.properties is used in environmental setup with the Configurtaion.java implementation.
 * @author abc
 *
 */
public class PropertiesFileReader {
	public Configuration getPropertiesFileData(String filePath) {
		FileReader reader;
		Configuration conf = new Configuration();
		try {
			reader = new FileReader(filePath);
			Properties p=new Properties();  
			p.load(reader); 

//			conf.setUdid(p.getProperty("udid"));
//			conf.setLuanchAppFrom(p.getProperty("luanchAppFrom"));
//			conf.setUseDefaultServer(p.getProperty("useDefaultServer"));
//			conf.setAppActivity(p.getProperty("appActivity"));
//			conf.setAppPackage(p.getProperty("appPackage"));
//			conf.setAppApkPath(p.getProperty("appApkPath"));

		} catch (FileNotFoundException e) {
			System.out.println("Fail to find file");
		} catch (IOException e) {
			System.out.println("Fail to read file");
		}  
		return conf;
	}

	public static void main(String args[]){
		PropertiesFileReader file=new PropertiesFileReader();
		file.getPropertiesFileData("Configuration.properties");
	}
}
