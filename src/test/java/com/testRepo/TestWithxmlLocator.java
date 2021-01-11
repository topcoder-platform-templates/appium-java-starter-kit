package com.testRepo;


import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

//import org.springframework.context.annotation.Description;
import org.testng.annotations.Listeners;

import com.baseRepo.*;
import com.utilities.EnumConstants.Orientation;

import com.pageRepo.*;


/**
 * this test is just for to check the functioning of the xml locators efficiency
 * @author abc
 *
 */
@Listeners(value = com.testRepo.TestSnapListener.class)
public class TestWithxmlLocator extends TestMaster {

	String initialLayout;
	String screenShotFileName;

	@Test(priority = 0, description = "Verify the Login process and confirm the user lands to HomePage")
	public void testCase01 () throws Exception {

		xmlTest = new PageWithTheXMLElementsLocators(driver, extentLogger); 
		
		// check for the xml element locator reader
		xmlTest.accessibilityTest();
	}

}