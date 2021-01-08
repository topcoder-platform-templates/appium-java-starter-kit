package com.testRepo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.baseRepo.BaseParent;
import com.environmentalSetup.EnviSetup;
import com.pageRepo.HomePage;
import com.pageRepo.LoginPage;
import com.pageRepo.PageWithTheXMLElementsLocators;
import com.utilities.ApkLauncher;
import com.utilities.GlobalVariables;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.service.local.AppiumDriverLocalService;

public class TestMaster {

	AppiumDriverLocalService service ;
    AppiumDriver<MobileElement> driver;
	public AppiumDriver<MobileElement> getDriver() {
		return driver;
	}

	public void setDriver(AppiumDriver<MobileElement> driver) {
		this.driver = driver;
	}

	public static String rootDir =  System.getProperty("user.dir");;
	public static Properties configProp;
	static ExtentReports extent;
	static ExtentTest extentLogger ;
	public Method method;
	
	static ExtentHtmlReporter reporter ;
	Logger logger = Logger.getLogger(TestMaster.class);

	//Pages
	LoginPage loginPage;
	HomePage homePage;
	BaseParent baseParent;
	BaseParent base;
	PageWithTheXMLElementsLocators xmlTest;
	
	/**
	 * initializes the test environment
	 * @throws MalformedURLException
	 */
	@BeforeSuite(alwaysRun = true)
	public void beforeSuite() throws MalformedURLException {
		GlobalVariables.BASE_DIR = rootDir;
		EnviSetup.readConfigProperties();
		reporter = new ExtentHtmlReporter("report\\report.html");
		extent = new ExtentReports();
		
		extent.attachReporter(reporter);
		System.setProperty("org.freemarker.loggerLibrary", "none");
		
	}

	@AfterTest(alwaysRun = true)
	public void afterTest() {

	}
	
	@BeforeClass
	public void beforeClass(){
		driver = ApkLauncher.getDriverExecutable();
	}


	@BeforeTest(alwaysRun = true)
	public void beforeTest() {

	}

	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method) {
		this.method = method;
		extentLogger = extent.createTest(method.getName());
		logger.info(method.getName() + " started");
	}

	/**
	 * use to log the status of the method if failed, passed and skipped
	 * @param result
	 * @throws IOException
	 */
	@AfterMethod(alwaysRun = true)
	public void afterMethod(ITestResult result) throws IOException {
		String screenshotPath = null;
		if (result.getStatus() == ITestResult.FAILURE) {
			screenshotPath = getScreenshot(getDriver(), method.getName());
			extentLogger.log(Status.FAIL, MarkupHelper.createLabel(result.getName() + " FAILED ", ExtentColor.PINK));
			extentLogger.fail(result.getThrowable(), MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			extentLogger.log(Status.PASS, MarkupHelper.createLabel(result.getName() + " PASSED ", ExtentColor.GREEN));
		} else {
			extentLogger.log(Status.SKIP, MarkupHelper.createLabel(result.getName() + " SKIPPED ", ExtentColor.YELLOW));
			extentLogger.skip(result.getThrowable());
		}

		logger.info(result.getMethod().getMethodName() + " ended");
		extent.flush();			
	}

	/**
	 * capture the screen shot and store at the report location for the extent Report
	 * @param driver
	 * @param methodName
	 * @return
	 */
	public String getScreenshot(AppiumDriver<MobileElement> driver, String methodName) {
		File destFile = null;
		
		String timeStamp = getTimeStamp("hh_mm_ss");
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		try {
		String reportDirectory = new File(rootDir).getAbsolutePath() + "/report";
		destFile = new File((String) reportDirectory+"/failure_screenshots/"+timeStamp+".png");
			FileUtils.copyFile(scrFile, destFile);
			extentLogger.log(Status.FAIL, "<span style=\"color:red\"> Test failed for  <strong>" + methodName + " Class, </strong></span> <a style=\"background: red;\" href='"+ destFile.getAbsolutePath() + "'> <img src='"+ destFile.getAbsolutePath() + "' height='100' width='100'/> </a>");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return destFile.getPath();
	}

	@AfterSuite(alwaysRun = true)
	public void afterSuite() {
		try {
			if(driver!=null)
			driver.quit();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * returns the current time in string format
	 * @param timeFormat like hh_mm_ss or dd_MM_yyyy_hh_mm_ss OR hh:mm:ss are some of the acceptable formats
	 */
	public static String getTimeStamp(String timeFormat) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formater = new SimpleDateFormat(timeFormat);
		String timeStamp = formater.format(calendar.getTime());
		return timeStamp;
	}
	
	@AfterClass
	public void afterClass() {
	}
}

