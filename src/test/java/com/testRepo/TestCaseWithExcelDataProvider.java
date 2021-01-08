package com.testRepo;

import static org.testng.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.baseRepo.BaseParent;
import com.baseRepo.Constant;
import com.environmentalSetup.EnviSetup;
import com.pageRepo.HomePage;
import com.pageRepo.LoginPage;
import com.pageRepo.PageWithTheXMLElementsLocators;
import com.utilities.ApkLauncher;
import com.utilities.GlobalVariables;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.service.local.AppiumDriverLocalService;

public class TestCaseWithExcelDataProvider {

	AppiumDriver<MobileElement> driver;
	public AppiumDriver<MobileElement> getDriver() {
		return driver;
	}

	public static String rootDir =  System.getProperty("user.dir");;
	public static Properties configProp;
	static ExtentReports extent;
	static ExtentTest extentLogger ;
	public Method method;

	static ExtentHtmlReporter reporter ;
	Logger logger = Logger.getLogger(TestMaster.class);

	//	//Pages
	LoginPage loginPage;
	HomePage homePage;
	BaseParent baseParent;
	BaseParent base;
	PageWithTheXMLElementsLocators xmlTest;


	//Create the Data Provider and give the data provider a name
	@DataProvider(name="Community-userIds-passwords-Excel-data-provider")
	public String[][] userIdsAndPasswordsDataProvider() {
		return XcelDataProvider.readExcelInto2DArray(GlobalVariables.BASE_DIR + "/TestData.xlsx", "Sheet1", 3);
	}	

	@BeforeTest
	public void beforeTest() {
		GlobalVariables.BASE_DIR = rootDir;
		EnviSetup.readConfigProperties();
		reporter = new ExtentHtmlReporter("report\\report.html");
		extent = new ExtentReports();

		extent.attachReporter(reporter);
		System.setProperty("org.freemarker.loggerLibrary", "none");
	}
	
	
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method) {
		this.driver = ApkLauncher.launchApp(Constant.appPackage, Constant.appActivity);
		this.method = method;
		extentLogger = extent.createTest(method.getName());
		logger.info(method.getName() + " started");
	}

	//Uses the data provider
	@Test(dataProvider="Community-userIds-passwords-Excel-data-provider")
	public void loginWithDataProvider(String communityName, String email, String password ) {
		
		//Page initiation
		loginPage = new LoginPage(getDriver(), extentLogger); 
		baseParent = new BaseParent(getDriver(), extentLogger); 
		homePage = new HomePage(getDriver(), extentLogger);
		
		// Login with the valid creds.
		 loginPage.login(communityName, email, password);

		// Verify the user successfully log in and land on the main screen.
		assertTrue (homePage.confirmHomePage());
	}

	@AfterMethod 
	public void tearDown () {
		driver.quit();
	}
	}