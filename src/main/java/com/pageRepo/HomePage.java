package com.pageRepo;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;  

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.aventstack.extentreports.ExtentTest;
import com.baseRepo.*;
import com.utilities.EnumConstants.Locator;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

public class HomePage extends BaseParent {

	private AppiumDriver<MobileElement> driver;
	Logger logger = Logger.getLogger(HomePage.class);
	// Page Factory elements  *************************
	
	private @FindBy(id = "alertTitle")
	List<WebElement>  switchLanguageAlert;

	private @FindBy(id =  "button1") 
	WebElement  okButton;

	private @FindBy(id =  "nav_header_title") 
	WebElement  navHeader;

	private @FindBy(xpath =  "//*/android.widget.TextView[@text ='Home']") 
	WebElement  homePageHomeIcon;
	// End of Page Factory eleemnts ******************


	// setting the driver for the class
	public void setDriver(AppiumDriver<MobileElement> driver) {
		this.driver = driver;
	}

	/**
	 * Login with the valid credentials
	 */
	public HomePage(AppiumDriver<MobileElement> driver,ExtentTest extentLogger) {
		super(driver,extentLogger);
		PageFactory.initElements(driver, this);
		this.setDriver(driver);
	
	}

	/**
	 * Reads and prints the title of the home page and 
	 * verify the home icon in the home page
	 * @return
	 *  the presence of the 'Home' icon in the home page ? / <br>
	 *  After login, do the user is navigated to the home page ?
	 */
	public Boolean confirmHomePage () {

		// catch the alert if present,
		try {
			getDriver().switchTo().alert().accept();
			getDriver().switchTo().defaultContent();
			log(1,"alert handeled with swithing to alert");
		} 
		catch (Exception e) {

			// if alert is not present, check alert is appearing ?
			if (switchLanguageAlert.size()>0) {
				log(1, "language switch alert appers");

				// accept alert by taping 'Ok'
				clickIt(okButton, "Ok Button");
			}
		}
		
		// print the heading of the homePage/Landing Page after Login
		log(1,navHeader.getText() + " appears at Home Screen");

		// return the ' Logged in workspace' status?
		return homePageHomeIcon.isDisplayed();
	}
}

