package com.pageRepo;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
//import org.testng.Reporter;

import com.aventstack.extentreports.ExtentTest;
import com.baseRepo.*;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
//import io.appium.java_client.android.AndroidDriver;
//import io.appium.java_client.android.AndroidElement;

public class LoginPage extends BaseParent {

	private AppiumDriver<MobileElement> driver;
	int elementLoadWaitTime = Constant.elementLoadingTime;
	
	Logger logger = Logger.getLogger(LoginPage.class);


	private @FindBy(id= "progress_bar")
	List<WebElement>  progressBar;

	private @FindBy(id= "com.Slack:id/sign_in_button")
	WebElement  loginButton;

	private @FindBy(xpath ="//*/android.widget.Button[@text='I’ll sign in manually']")
	WebElement  iWillSignInManually ;

	private @FindBy(id = "team_url_edit_text")
	WebElement  enterYourWorkspaceURL;

	private @FindBy(className =  "android.widget.Button")
	WebElement  next;

	private @FindBy(id = "email_edit_text")
	WebElement  emailAddress;

	private @FindBy(id = "password_edit_text")
	WebElement  passwordInputField;

	private @FindBy(xpath = "//*/android.widget.TextView[@content-desc = 'Show password']")
	WebElement  showPassword;

	private @FindBy(xpath = "//*/android.widget.TextView[@content-desc = 'Hide password']")
	WebElement  hidePassword;

	private @FindBy(xpath =  "//*/android.widget.Button[@text ='Next']")
	WebElement  nextButton;

	private @FindBy(xpath =  "//*/android.widget.ImageView[@content-desc='Loading']")
	List<WebElement>  loadingSlack;

	/**
	 * Login Page Constructor inintializing the driver and logger
	 */
	public LoginPage(AppiumDriver<MobileElement> driver, ExtentTest extentLogger) {
		super(driver,extentLogger);
		PageFactory.initElements(driver, this);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	/**
	 * Over ridden method of ({@link com.pageFiles.LoginPage#login(String, String, String, boolean)} loginMethod
	 * @param workSpace : Name of the Workspace.
	 * @param email : Email address which is being used for Login to 'Slack'.
	 * @param password : password associated with the email.
	 * @return the suitable driver according to the valid login data provided, return 
	 */
	public Object login (String workSpace, String email, String password) {
		return login (workSpace, email, password, "true");
	}

	/**
	 * perform the login preocess with the given parameters in 'Slack' app
	 * it accepts the parameter both for <strong> valid </strong>as well as  <strong>invalid</strong> login tests
	 * @param workSpace : Name of the Workspace
	 * @param email : Email address which is being used for Login to 'Slack'
	 * @param password : password assoicated with the email
	 * @param isValidLogin : boolean (as String) for, is this a valid login Test / or invalid login Test?
	 * @return The
	 */
	public Object login (String workSpace, String email, String password,  String isValidLogin) {

		boolean status = true ;
		boolean isValid = Boolean.valueOf(isValidLogin);

		// wait for laoding the landing/LoginPage
		waitForLoading(progressBar, elementLoadWaitTime);

		// Click the 'sign In button'
		clickIt(loginButton, "SIGN IN");

		// click the 'I'll sign in manually'
		clickIt(iWillSignInManually, "I'll sign in manually");

		// type the workspace URL
		typeTextTo (enterYourWorkspaceURL, workSpace, "Worksapce URL");

		// click next to proceed
		clickIt(next, "Next");

		// type the email
		typeTextTo (emailAddress, email, "Email Address");

		// click next to proceed
		clickIt(next, "Next");

		// Time to check for security
		clickIt(showPassword, "Visibility Eye for show password");

		// type the password 
		typeTextTo (passwordInputField, "Security", "Password field");
		log(1, "I forgot to uncheck the 'show password' option, Unchecking now the 'Show Password'");

		// Hide the typed password
		hidePassword.click();
		typeTextTo (passwordInputField, password, "Password field", true);

		// click next to proceed
		clickIt(nextButton, "Next Button");

		try {
			waitForLoading(loadingSlack, elementLoadWaitTime);
			//			waitToDisappear(loadingSlack, 10);

		} catch (TimeoutException timeout) {
		} catch (IndexOutOfBoundsException indexOutOfBound) {
		} catch (Exception e) {
			status = false;
		} 

		// status of logging in process completed / failed
		if (status==false) {
			log(2,"Failed to Login"); 
		} else {
			log(1,"I completed with the Login process successfully");
		}
		

		return isValid ? new HomePage(driver, extentLogger) :  driver; 
		// return the driver to the suitable page
		//		if (isValid) {
		//			return new HomePage(driver); 
		//		} else {
		//			return driver;
		//		}
		
	}


	private void waitToDisappear(List<WebElement> ele, long timeInSeconds) {
		WebDriverWait wdw = new WebDriverWait(getDriver(), timeInSeconds);
		wdw.until(ExpectedConditions.invisibilityOf(ele.get(0)));
	}

	/**
	 * wait until the elements like loading image, or spinner gets disappears
	 * 
	 * @param ele : element displayed while loading 
	 * @param timeInSeconds driver waits for loading the element up to this duration.
	 */
	private void waitForLoading(List<WebElement> ele, int timeInSeconds) {

		for(int i=0;i<=timeInSeconds;++i){
			if((ele).isEmpty()){
				log (1, "waited for " + (i) + " seconds for loading page.");
				break;
			} 
			else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// sets the driver for the class
	public void setDriver(AppiumDriver<MobileElement> driver) {
		this.driver = driver;
	}
}

