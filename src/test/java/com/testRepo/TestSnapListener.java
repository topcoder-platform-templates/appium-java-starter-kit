package com.testRepo;

import java.io.File;
import java.io.IOException;

import org.apache.commons.*;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.Markup;
import com.baseRepo.*;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

/**
 * specially used when the TestNG is only reporting tool used for reporting not necessary for extent logger
 * @author abc
 *
 */
public class TestSnapListener  extends TestListenerAdapter {
	private ExtentTest extentLogger;
	private AppiumDriver<MobileElement> driver;
	BaseParent base = new BaseParent(driver, extentLogger);
	@Override
	public void onTestFailure(ITestResult result) {
		extentLogger = TestCase01.extentLogger;
		String timeStamp = base.getTimeStamp("dd_MM_yyyy_hh_mm_ss");
		String methodName = result.getName();
		if(!result.isSuccess()){
			File scrFile = ((TakesScreenshot)base.getDriver()).getScreenshotAs(OutputType.FILE);
			try {
				String reportDirectory = new File(System.getProperty("user.dir")).getAbsolutePath() + "/target/surefire-reports";
				File destFile = new File((String) reportDirectory+"/failure_screenshots/"+methodName+"_"+timeStamp+".png");
				FileUtils.copyFile(scrFile, destFile);
				Reporter.log("<span style=\"color:red\"> Test failed for  <strong>" + methodName + " Class, </strong></span> <a style=\"background: red;\" href='"+ destFile.getAbsolutePath() + "'> <img src='"+ destFile.getAbsolutePath() + "' height='100' width='100'/> </a>");
//				extentLogger.log(Status.FAIL, "<span style=\"color:red\"> Test failed for  <strong>" + methodName + " Class, </strong></span> <a style=\"background: red;\" href='"+ destFile.getAbsolutePath() + "'> <img src='"+ destFile.getAbsolutePath() + "' height='100' width='100'/> </a>");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
