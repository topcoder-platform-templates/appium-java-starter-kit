package com.testRepo;


import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

//import org.springframework.context.annotation.Description;
import org.testng.annotations.Listeners;

import com.baseRepo.*;
import com.utilities.EnumConstants.Orientation;

import com.pageRepo.*;


@Listeners(value = com.testRepo.TestSnapListener.class)
public class TestCase01 extends TestMaster {

	String initialLayout;
	String screenShotFileName;

	@Test(priority = 0, description = "Verify the Login process and confirm the user lands to HomePage")
	public void testCase01 () {

		loginPage = new LoginPage(driver, extentLogger); 
		homePage = new HomePage(driver, extentLogger);
		// Login with the valid creds.
//		homePage = (HomePage) 
				loginPage.login(Constant.workspaceName, Constant.email, Constant.password);

		// Verify the user successfully log in and land on the main screen.
		assertTrue (homePage.confirmHomePage());
	}


	@Test(priority = 1, description = "Verify the screen Orienatation and capture the screenshot")
	public void testCase02 () {
		baseParent = new BaseParent (driver, extentLogger);

		// Set Screen Orientation to LANDSCAPE view
		initialLayout = baseParent.checkOrientation();
		baseParent.changeScreenOrientation (Orientation.LANDSCAPE);

		// check the orientation
		assertTrue(baseParent.checkOrientation().equalsIgnoreCase("LANDSCAPE.toString())"));
		//		assertTrue(baseParent.checkOrientation().equalsIgnoreCase(Orientation.LANDSCAPE.toString()));

		// Take and Save the screenshot to the current root folder
		if (baseParent.checkOrientation().equalsIgnoreCase(Orientation.LANDSCAPE.toString()))  {
			//		screenShotFileName = catchScreenAtRoot(Constant.screenshotFileName);
			screenShotFileName = homePage.catchScreenAtRoot(Constant.screenshotFileName);
		}

		// check Screenshot file is created or Not?
		assertTrue(baseParent.isFileCreatedAtRootFolder(screenShotFileName));

		// change orientation to previous state (default);
		baseParent.changeScreenOrientation (initialLayout);
	}

	@Test(priority = 3, description =  "Verify the log status printed as per the status code")
	public void testCase03 () {
		baseParent = new BaseParent (driver, extentLogger);
		baseParent.log(4, "This is Test Error Log");
		baseParent.log(5, "This is Test info");
		baseParent.log(6, "This is Test Warning Log");
		baseParent.log(9, "This is Test Default Log With Number 9");
		baseParent.log(7, "This is Test Fatal Log");
	}

	@Test(enabled = false, description = " This is test for status checks", dependsOnMethods = {"testCase02"})
//	@Description(value = "Verify the Test is skipped")
	public void testCase04 () {
		baseParent = new BaseParent (driver, extentLogger);
		baseParent.log(5, "This is Skipped Test");
	
	}	
}