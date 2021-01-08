package com.baseRepo;

public class Constant {
	public static String workspaceName = "skillbuilderworkspace";
	public static String email = "mailmemakar@gmail.com";
	public static String password = "Topcoder@123";
	
	public static String deviceName = "TestDevice";//"Please Change Me";
	public static String androidVersion = "8.1"; //"Please Change Me";
	public static String automationName = "UiAutomator1"; //"Please Change Me";
	public static final String platformName = "Android"; //"Please Change Me";
	public static long adbExecTimeout = 30000; //"Please Change Me";
	public static int testdroid_testTimeout = 300;
	public static String screenshotFileName = "SlackApp";
	public static int elementLoadingTime = 30; // depending on the network connectivity and the device performance;
	public static String BASE_DIR = "";
	
	// alternatively configuration.properties file values  can be defined here
	public static String udid = "emulator-5554";//"Please Change Me";
	public static boolean useDefaultServer = true;//"Please Change Me";
	public static String appPackage = "com.Slack"; 
	public static String appActivity = "com.Slack.ui.HomeActivity";
	public static String luanchAppFrom = "apkFile"; // "packageAndActivity"
	public static String appApkPath = "./src/test/resources/appRepo/ApiDemos-debug.apk"; // "PACKAGE/ACTIVITY"
}
