package com.utilities;

import java.util.Hashtable;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;

import com.utilities.EnumConstants.Locator;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

public class ElementTracer {
	AppiumDriver<MobileElement> driver = null;
	Hashtable<String, String> identifierList = new Hashtable<String, String>();
	
	public ElementTracer(AppiumDriver<MobileElement> driver, Hashtable<String, String> identifierList) {
		this.driver = driver;
		this.identifierList = identifierList;
	}

	public MobileElement findElement(Locator locator, String identifier) throws Exception {
		MobileElement mobileElement = null;
		int count = 0;
		do {
			try {
				String elementToSearch = identifierList.get(identifier);
				switch(locator.toString()) {
				case "XPATH":
					mobileElement = driver.findElementByXPath(elementToSearch); break;
				case "ID":
					mobileElement = driver.findElementById(elementToSearch); break;
				case "CLASSNAME":
					mobileElement = driver.findElementByClassName(elementToSearch); break;
				}
			} catch(WebDriverException wde) {
				continue;
			}
			
			if(mobileElement != null) {
				break;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			count++;
			if(count==59) {
				System.out.println();
			}
		} while(count < 10);
		if (count == 10) {
			throw new Exception("Failed to find element with identifier: " + identifier +" after waiting for 1 Minute.");
		}
		return mobileElement;
	}

	public MobileElement waitForElementToBeDisplayed(Locator locator, String identifier) throws Exception {
		MobileElement webElement = null;
		int count=0;
		do {
			webElement = findElement(locator, identifier);
			if(webElement.isDisplayed()) {
				break;
			}
			Thread.sleep(1000);
			count++;
		} while(count<60);
		if(count == 60) {
			throw new Exception("Element is not displayed in 1 Minute");
		}

		return webElement;
	}

	public MobileElement waitForElementToBeEnabled(Locator locator, String identifier) throws Exception {
		MobileElement webElement = null;
		int count=0;
		do {
			webElement = findElement(locator, identifier);
			if(webElement.isEnabled()) {
				break;
			}
			Thread.sleep(1000);
			count++;
		} while(count<60);
		if(count == 60) {
			throw new Exception("Element is not enabled in 1 Minute");
		}
		return webElement;
	}

	public MobileElement waitForElementToBeEnabledAndDisplayed(Locator locator, String identifier) throws Exception {
		MobileElement webElement = null;
		int count=0;
		do {
			webElement = findElement(locator, identifier);
			if(webElement.isEnabled() && webElement.isDisplayed()) {
				System.out.println(webElement.getText());
				break;
			}
			Thread.sleep(1000);
			count++;
		} while(count<60);
		if(count == 60) {
			throw new Exception("Element is not displayed and enabled in 1 Minute");
		}
		return webElement;
	}
}
