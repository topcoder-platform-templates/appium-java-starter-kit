package com.pageRepo;

import java.util.List;

import com.aventstack.extentreports.ExtentTest;
import com.baseRepo.BaseParent;
import com.utilities.ElementTracer;
import com.utilities.EnumConstants.Locator;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidElement;

public class PageWithTheXMLElementsLocators extends BaseParent {

	public PageWithTheXMLElementsLocators(AppiumDriver<MobileElement> driver, ExtentTest extentLogger) {
		super(driver, extentLogger);
	}


	/**
	 * clicks on the accessibiltyId in the demoApk app
	 * This is to test the element finder and XML path elementTracer classes
	 * @throws Exception
	 */
	public void accessibilityTest () throws Exception {
		MobileElement accessibility = elementTracer.findElement(Locator.XPATH, "accesabilityId");
		clickIt(accessibility,"Accesibility");

		List<MobileElement> eleList = getDriver().findElementsByClassName("android.widget.TextView");

		for (int i = 0; i < eleList.size(); i++) {
			try {
				log (1, eleList.get(i).getText() + " is element ;");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
