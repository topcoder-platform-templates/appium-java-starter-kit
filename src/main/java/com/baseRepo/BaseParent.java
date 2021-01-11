package com.baseRepo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Pattern;

import static io.appium.java_client.touch.TapOptions.tapOptions;
import static io.appium.java_client.touch.LongPressOptions.longPressOptions;
import static java.time.Duration.ofSeconds;
import static io.appium.java_client.touch.offset.ElementOption.element;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.net.UrlChecker.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.google.common.base.Preconditions;
import com.utilities.ApkLauncher;
import com.utilities.ElementTracer;
import com.utilities.EnumConstants.Direction;
import com.utilities.EnumConstants.Orientation;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.touch.TapOptions;
import io.appium.java_client.touch.offset.PointOption;



import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.touch.TapOptions;
import io.appium.java_client.touch.offset.PointOption;

/**
 * Base class for all the classes, it includes 
 * <ul>
 *		<li> Method {@link com.baseFiles.TheBase#log(String)} </li>
 *		<li> Method {@link com.baseFiles.TheBase#catchScreenAtRoot(String)} </li>
 *		<li> Method {@link com.baseFiles.TheBase#typeTextTo(WebElement, String, String, boolean)} </li>
 *		<li> Method {@link com.baseFiles.TheBase#changeScreenOrientation(String)} </li>
 *		<li> Method {@link com.baseFiles.TheBase#checkOrientation()} </li>
 *		<li> Method {@link com.baseFiles.TheBase#isFileCreated(String)} </li>
 *		<li> Method {@link com.baseFiles.TheBase#typeTextTo(WebElement, String, String, boolean)} </li>
 * </ul>
 * @author abc
 *
 */
public class BaseParent {

	AppiumDriver<MobileElement> driver;
	Logger logger = Logger.getLogger(BaseParent.class);
	TouchAction touchAction;
	protected ExtentTest extentLogger;
	String className = this.getClass().getSimpleName();
	protected ElementTracer elementTracer = null;
	protected Hashtable<String, String> xpathFinder = new Hashtable<>();
	List<String> alreadyCalledClasses = new ArrayList<String>();



	public BaseParent(AppiumDriver<MobileElement> driver, ExtentTest extentLogger) {
		this.driver = driver;
		this.extentLogger = extentLogger;
		
		if (this.getClass() != BaseParent.class && !alreadyCalledClasses.contains(className)) {
			xpathFinder= 	xmlPathList();
		}
		elementTracer = new ElementTracer(driver, xpathFinder);
	}



	/**
	 * Prints the logs (The Message which should be displayed in log/console output)
	 *  to the console as well as
	 * it serves the Reporter.log of TestNG reporting 
	 * @param 
	 * 
	 * */
	public void log (int statusCode, String logMessage) {
		Status status = null;
		switch (statusCode) {
		default:
		case 1: 
			status = Status.PASS;
			break;
		case 2: 
			status = Status.FAIL;
			break;
		case 3: 
			status = Status.SKIP;
			break;
		case 4: 
			status = Status.ERROR;
			break;
		case 5: 
			status = Status.INFO;
			break;
		case 6: 
			status = Status.WARNING;
			break;
		case 7: 
			status = Status.FATAL;
			break;
		}
		logMessage.charAt(logMessage.length()-1);
		if (!logMessage.substring(logMessage.length()-1,logMessage.length()).equalsIgnoreCase(".")) {
			logMessage = logMessage +".";
		}
		Calendar.getInstance();
		new SimpleDateFormat("hh:mm:ss");
		System.out.println("\t\t" + logMessage + "\n");
		//		extentLogger.log(Status.PASS, logMessage);
		Reporter.log( "<li style =\""
				+ "background: #00FF7F;"
				+ " padding: 2px;"
				+ " font-size: 13px;"
				+ " margin-left: 20px;"
				+ " list-style-type: square;"
				+ " list-style-position: inside;"
				+ "color: #4B0082;"
				+ "\" > <span style=\"color:black\">"
				+ getTimeStamp("hh:mm:ss")
				+ "</span>   >  \t "
				+ logMessage 
				+ "</li>");
		Reporter.log("<hr style =\"width: 80%; color: #800000\" >");

		extentLogger.log(status, "<span style =\""
				+ "background: #CAFAFE;"
				+ " padding: 2px;"
				+ " font-size: 14px;"
				+ " margin-left: 10px;"
				+ "color: #4B0082;"
				+ "\" >"
				+ logMessage 
				+ "</span>");
	}


	/**
	 * It takes the screenshot and store to the root location with the fileName passed as Parameter
	 * @param fileName
	 * @return 
	 */
	public String catchScreenAtRoot(String fileName) {
		fileName = fileName + getTimeStamp("dd_MM_yyyy_hh_mm_ss");
		File screenShot;
		screenShot = new File("./" + fileName +".png");

		BufferedImage bImage = null;
		File imageFile =  driver.getScreenshotAs(OutputType.FILE);
		try {
			bImage = ImageIO.read(imageFile);
			ImageIO.write(bImage, "png", screenShot);
		} catch (IOException e) {
			log(2, "Exception occured while taking Screen Shot :" + e.getMessage());
		}

		log(1, "Screen shot captured and placed at Root directrory as '" 
				+ fileName+ ".png' in the project ");
		return fileName;
	}

	/**
	 * sends the textString to the input fields and log the message.
	 * It override the original method
	 * 
	 * <a href="##"> Original method</a>
	 * @param ele
	 * @param inputText
	 * @param elementName
	 */
	protected void typeTextTo(WebElement ele, String inputText, String elementName) {
		typeTextTo(ele, inputText, elementName, false);
	}

	/**
	 * The method use for clicking the Webelement and write an action log to the reporter.
	 * 
	 * @param element
	 * @param elementName
	 */
	protected void clickIt(WebElement element, String elementName) {
		element.click();
		log (1, "I clicked '" + elementName + "'.");
	}

	/**
	 * Check the String contains numbers only
	 * @param strNum
	 * @return
	 */
	public boolean isNumeric(String strNum) {
		Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
		if (strNum == null) {
			return false;
		}
		return pattern.matcher(strNum).matches();
	}
	
	
	/**
	 * Returns the status of the Webelement present inside the frames 
	 * specially used for Hybrid applications
	 * 
	 * @param ele List of webelement (Actually the single element should also be passed as list, thus the driver can check all the element with the 
	 * @return The presence of the Web element inside the frmae in mobile browser
	 */
	public WebElement getPresenseOfElementsInFrame(List<WebElement> ele) {

		WebElement activeElement1 = driver.switchTo().activeElement();
		if (!activeElement1.getAttribute("class").isEmpty() || !activeElement1.getAttribute("class").isEmpty()) {
			System.out.println(
					activeElement1.getAttribute("class"));
		}
		if (!activeElement1.getAttribute("id").isEmpty() || !activeElement1.getAttribute("id").isEmpty()) {
			System.out.println(
					activeElement1.getAttribute("id"));
		}
		int getTotalFrames = driver.findElements(By.tagName("iframe")).size();

		for(int i = 0 ; i<getTotalFrames;i++) {
			driver.switchTo().frame(i);
			List<WebElement> numbersOfElementsPresent = ele;
			if (!numbersOfElementsPresent.isEmpty()) {
				return numbersOfElementsPresent.get(i);
			}
		}
		return null;
	}

	
	/**
	 * This method overrides {@link com.baseFiles.TheBase#changeScreenOrientation(String)}
	 * It accepts the {@link com.baseFiles.Orientation} enum and sets the orientation accordingly.
	 * @param orientation
	 */
	public void changeScreenOrientation(Orientation orientation) { 
		changeScreenOrientation(orientation.toString());
	}



	/**
	 * changes the layout as per the option passed as parameter
	 * 
	 * @param layout
	 * @return The 
	 */
	public boolean changeScreenOrientation(String layout) { 
		switch (layout) {
		default:
		case "PORTRAIT":
			driver.rotate(ScreenOrientation.PORTRAIT);
			break;
		case "LANDSCAPE":
			driver.rotate(ScreenOrientation.LANDSCAPE);
			break;
		}
		log(1, "Screen Orientation is set to " + layout);

		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return checkOrientation().equalsIgnoreCase(layout);
	}


	/**
	 * checks and return the screen orientation.
	 * 
	 * When using this with a real device, the device should not be moved so that the built-in sensorsdo not interfere.
	 * 
	 * @return <strong> The current layout/orientation of the screen</strong>
	 */
	public String checkOrientation() {
		String layoutOrientation;
		//			ScreenOrientation orientation = driver.getOrientation() ;
		layoutOrientation = driver.getOrientation().toString();
		return layoutOrientation;
	}

	
	/**
	 * checks and return the boolean status of the file is created/ not
	 * 
	 * @param fileName
	 * @return whether the file is existing with the given name
	 */
	public boolean isFileCreatedAtRootFolder(String fileName) {
		File file = new File ("./" + fileName +".png");
		return file.exists();
	}
	
	/**
	 * Checks the existance of the file at the given location
	 * 
	 * @param filePath the file path ending with '/' 
	 * @param fileName the name of the file to be checked for
	 * @param fileExtension the file extension like, '.png', '.txt'
	 * @return the file is existing at the given locaiton/ not?
	 *
	 */
	public boolean isFileCreated(String filePath, String fileName, String fileExtension) {
		File file = new File (filePath + fileName +fileExtension);
		return file.exists();
	}


	/**
	 * Actual method to sends the textString to the input fields and log the message.
	 * It override the original method, checks the password is passed on to the input 
	 * and checks the visibility icon to invisible the password.
	 * @param webelement
	 * @param inputText
	 * @param elementName
	 * @param inputIsPassword
	 * 
	 * @see com.baseFiles.TheBase#typeTextTo(WebElement, String, String)   <p> Over ridden Methods </p>
	 */
	protected void typeTextTo(WebElement webelement, String inputText, String elementName, boolean inputIsPassword) {
		boolean status = true;
		try {
			webelement.sendKeys(inputText);
			if (inputIsPassword) {
				log(1, "I typed '" + inputText.substring(0, inputText.length()/2)+ "****' to the '"
						+ elementName.toUpperCase() + "' field");
			} 
			else log(1, "I typed '" + inputText + "' to the '" + elementName.toUpperCase() + "' field");
			
		} catch (Exception e) {
			status = false;
			log (2, "Failed to Type " + inputText + " to " + webelement + " Field.");
			extentLogger.fail(e);
			e.printStackTrace();
		}

	}



	/**
	 * the driver is made available to other classes
	 * 
	 * @return driver
	 */
	public static AppiumDriver<MobileElement> getDriver() {
		return ApkLauncher.getDriverExecutable();
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
	
	/*
	 * usingTexts>
	<usingText title="accesabilityId">
		<usingStr>//android.widget.TextView[@text = 'Accessibility']</usingStr>
	</usingText>
	 */
	
	
	
	
	public Hashtable<String, String> xmlPathList() {
		String key = "";
		
		String fileName = "./src/main/resources/Locators/" + className + ".xml";
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try  {  
			// build DOM builder factory to craete document.
			builder = builderFactory.newDocumentBuilder();

			// parse XML files into DOM objects
			Document document = builder.parse(new File(fileName));

			// get Root Element for xml tree
			Element rootElement = document.getDocumentElement();

			// get childrens can be elements, comments, processing instructions,
			// characters etc,
			NodeList nodes = rootElement.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);

				// if node is an Element then process accordingly.
				if (node instanceof Element) {
					Element element =  (Element) node;

					key = (String) element.getAttribute("title");

					String xPath = element.getElementsByTagName("usingStr").item(0).getTextContent();

					if (xpathFinder.containsKey(key) && xpathFinder.get(xPath) != xPath) {
						System.out.println("duplicate key found in '" + fileName
								+ "' file and the duplicate key  is '" + key + "'");
						Error er = new Error("duplicate key found");
						System.out.println(er);
					}
					if (xpathFinder.contains(xPath) && xpathFinder.get(key) != key) {
						System.out.println("duplicate xPath found in '" + fileName
								+ "' file and the duplicate value is '" + xPath + "'");
						Error er = new Error("duplicate xPath Found");
						System.out.println(er);
					}

					xpathFinder.put(key, xPath);
				}
			}
		} catch (ParserConfigurationException e) {
			System.out.println("Fail to Parse: " + fileName + " file");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.out.println("Fail to find: " + fileName + " file");
		} catch (SAXException e) {
			System.out.println("Fail to SAXE: " + fileName + " file");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Fail to read: " + fileName + " file");
			e.printStackTrace();
		}
		return xpathFinder;
	}
	
	/**
	 * Predefined validation for the element is displayed, text validations
	 * and the placeholder validations
	 * @author abc
	 *
	 */
	public static class Validations {

		public void isElementDisplayed(MobileElement webElement) {
			Preconditions.checkNotNull(webElement, " Unable to find element ");
			Assert.assertTrue(webElement.isDisplayed(), "Web Element is not Displayed ");
		}

		public void isElementContainsText(MobileElement webElement, String expectedText) {
			Preconditions.checkNotNull(webElement, " Unable to find element ");
			String actualText = webElement.getText().trim();
			Assert.assertEquals(actualText, expectedText, "Validation Failed.");
		}

		public void isElementContainsPlaceholderText(MobileElement webElement, String expectedText) {
			Preconditions.checkNotNull(webElement, " Unable to find element ");
			String actualText = webElement.getAttribute("placeholder").trim().toLowerCase();
			Assert.assertEquals(actualText, expectedText.toLowerCase(), "Validation Failed.");
		}
	}
	
	public void switchButton(MobileElement element, Direction dir) { 
		@SuppressWarnings("rawtypes")
		TouchAction t1 = new TouchAction (getDriver());
		Point switch1Pos = element.getLocation();
		int x = switch1Pos.getX() + 10 ;
		int y = switch1Pos.getY();

		if (dir.equals("RIGHT")) {
			log(1, "Switch button tap to Right");
			y = y+10;
		}

		if (dir.equals("LEFT")) {
			y = y +50;
			log(1, "Switch button tap to Left");
		}
		t1.tap(TapOptions.tapOptions().withPosition(PointOption.point(x,y))).perform();
	}
	
	public void backToMenu(int i ) { 
		int tries = 0;
		do {
			getDriver().navigate().back();
			tries++;
			if (tries==i) {
				log(1, "pressed back button " + i + " Times");
				break;
			}
		} while (tries <i);
	}
	
	public void backToMenu(AndroidElement menuElement) { 
		int tries = 0;
		do {
			getDriver().navigate().back();
			tries++;
			if (menuElement.isDisplayed()) {
				log(1, " navigated back to the element ");
				break;
			}
			if (tries  >= 10) {
				log( 2, "Failed to navigate up to " + menuElement + " or element is not displayed or no defined eariler");
			}
		} while (! menuElement.isDisplayed() || tries ==3);
	}
	
	public void tap(AndroidElement element) { 
		@SuppressWarnings("rawtypes")
		TouchAction t = new TouchAction(getDriver());
		t.tap(tapOptions().withElement(element(element))).perform();
		log(1, "I tap the element");
	}

	public void longPress(AndroidElement element) { 
		@SuppressWarnings("rawtypes")
		TouchAction t = new TouchAction(driver);
		t.longPress(longPressOptions().withElement(element(element)).withDuration(ofSeconds(2))).release().perform();
		log(1, "I tap the element");
	}

	public void moveTo(AndroidElement fromElement, AndroidElement toElement ) { 
		@SuppressWarnings("rawtypes")
		TouchAction t = new TouchAction(driver);
		t.longPress(longPressOptions().withElement(element(fromElement)).withDuration(ofSeconds(2))).moveTo(element(fromElement)).release().perform();
		log(1, "I tap the element");
	}
	
//	public   AndroidElement scrollToElement(String elementAttrib, String elementValue) {
//		return getDriver().findElementByAndroidUIAutomator("new UiScrollable(new UiSelector()).scrollIntoView("
//				+  elementAttrib
//				+ "(\""
//				+ elementValue
//				+ "\"));");
//	}
}
