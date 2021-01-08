package com.utilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Reporter;

import com.baseRepo.BaseParent;
import com.baseRepo.Constant;
import com.environmentalSetup.EnviSetup;

//import com.baseFiles.Constant;
//import com.pageFiles.LoginPage;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

public class ApkLauncher {

	static AppiumDriver<MobileElement> driver;
	BaseParent base = null ;

	public  ApkLauncher(AppiumDriver<MobileElement> driver) {
		//		ApkLauncher.driver=driver;
	}

	public static AppiumDriver<MobileElement> getDriverExecutable() {
		String appLaunchOption = (String) EnviSetup.configProp.getProperty("luanchAppFrom"); 
		System.out.println(appLaunchOption);
		if(driver==null) {
			if (appLaunchOption.equalsIgnoreCase("packageAndActivity")) { 
				driver = launchApp(Constant.appPackage, Constant.appActivity);
			} else if (appLaunchOption.equalsIgnoreCase("apkFile")) {
				driver = launchApp(null, null);
			}
		}
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		return driver;
	}

	/**
	 * declaring Basic desired Capabilities for launching the app
	 * @return DesiredCapabilities
	 * */
	public static  DesiredCapabilities basicCaps() {
		DesiredCapabilities cap = new DesiredCapabilities();
		setCapabilities(cap, AndroidMobileCapabilityType.ADB_EXEC_TIMEOUT, Constant.adbExecTimeout);
		setCapabilities(cap, AndroidMobileCapabilityType.PLATFORM_NAME, Constant.platformName);
		setCapabilities(cap, MobileCapabilityType.DEVICE_NAME, Constant.deviceName);
		setCapabilities(cap, MobileCapabilityType.UDID, Constant.udid);
		setCapabilities(cap, MobileCapabilityType.PLATFORM_VERSION, Constant.androidVersion);
		setCapabilities(cap, MobileCapabilityType.AUTOMATION_NAME, Constant.automationName);
		return cap;
	}

	/**
	 * Setting out the basic capabilities.
	 */
	private static void setCapabilities(DesiredCapabilities desiredCapability, 
			String capabilityName, Object capabilityValue) {
		desiredCapability.setCapability(capabilityName, capabilityValue );
		log("'" + capabilityName + "' is set up with '" + capabilityValue + "' under Desired Capabilities.");
	}


	private static void log(String string) {
		Reporter.log(string);
	}

	/**
	 * The important method for launching and initiating the AppiumDriver
	 *
	 * @return AppiumDriver 
	 * <p> this driver is utilized by the sub classes </p>
	 * */
	public static AppiumDriver<MobileElement> launchApp(String appPackage,String appActivity) {

		// get the basic capabilities;
		DesiredCapabilities cap = basicCaps();

		// if App Launch option is selected as apk file locating app
		if (appPackage == null && appActivity == null) {
			// locating and installing .apk file with absolute path;
			//			File app = new File(GlobalVariables.BASE_DIR+ EnviSetup.configProp.getProperty("appApkPath"));
			File app = new File(Constant.appApkPath);
			setCapabilities(cap, MobileCapabilityType.APP, app.getAbsolutePath());

			//	installing the app with package name and package activity provided
		} else {
			// set the app package and activity
			setCapabilities(cap, AndroidMobileCapabilityType.APP_PACKAGE, appPackage);
			setCapabilities(cap, AndroidMobileCapabilityType.APP_ACTIVITY, appActivity);

			log("The " + appPackage.substring(appPackage.lastIndexOf('.')+1, appPackage.length())
			+ " is launched and started successfully");
		}

		// connecting with Appium server with Desired Capabilities / launching app on device;
		driver = getServer(cap);
//		try {
//			driver = new AppiumDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), cap);
//			log("The appium server is now connected.");
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//			log("The server fails to launch the session");
//		}

		// set wait timeout
		driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);

		// returning the driver from the method;
		return driver;
	}

	private static  AppiumDriver<MobileElement> getServer(DesiredCapabilities capabilities) {
		String useDefaultServer = (String) EnviSetup.configProp.get("useDefaultServer"); 
		if((useDefaultServer.equalsIgnoreCase("true")) ) {

			AppiumServiceBuilder serviceBuilder = new AppiumServiceBuilder();
			serviceBuilder.usingAnyFreePort();
			AppiumDriverLocalService service = AppiumDriverLocalService.buildService(serviceBuilder);
			//			service.start();
			driver = new AndroidDriver<MobileElement>(service,capabilities);
		}else {
			try {
				driver = new AndroidDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"),capabilities);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}	
		}
		return driver;
	}}
