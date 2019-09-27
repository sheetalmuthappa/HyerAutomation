package com.bluelabellabs.util;

import static io.appium.java_client.touch.WaitOptions.waitOptions;
import static io.appium.java_client.touch.offset.PointOption.point;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.testng.Assert;

import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.bluelabellabs.hyer.library.GenericLib;
import com.bluelabellabs.hyer.listener.MyExtentListners;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.ios.IOSDriver;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class MobileActionUtil {
	public final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	/**
	 * Description : This method has fluent wait implementation for element to
	 * load which is polling every 250 miliseconds
	 * 
	 * @param element
	 * @param driver
	 * @param eleName
	 * @throws IOException
	 */
	public static void waitForElement(WebElement element, IOSDriver driver, String elementName, int seconds)
			throws IOException {
		try {
			logger.info("---------Waiting for visibility of element---------" + element);

			Wait<IOSDriver> wait = new FluentWait<IOSDriver>(driver).withTimeout(seconds, TimeUnit.SECONDS)
					.pollingEvery(250, TimeUnit.MICROSECONDS).ignoring(NoSuchElementException.class);
			// Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(element))
			// !=
			// null);
			logger.info("---------Element is visible---------" + element);
		} catch (Exception e) {
			MyExtentListners.test.fail(MarkupHelper.createLabel("Verify " + "\'" + elementName + "\'"
					+ " is displayed || " + "\'" + elementName + "\'" + " is not displayed ", ExtentColor.RED));
			MyExtentListners.test.addScreenCaptureFromPath(capture(driver, elementName));
			logger.info("---------Element is not visible---------" + element);
			throw e;
		} catch (AssertionError e) {
			MyExtentListners.test.fail(MarkupHelper.createLabel("Verify " + "\'" + elementName + "\'"
					+ " is displayed || " + "\'" + elementName + "\'" + " is not displayed ", ExtentColor.RED));
			MyExtentListners.test.addScreenCaptureFromPath(capture(driver, elementName));
			logger.info("---------Element is not visible---------" + element);
			throw e;
		}
	}

	/**
	 * Description : This method has fluent wait implementation for element to
	 * load which is polling every 250 miliseconds
	 * 
	 * @param element
	 * @param driver
	 * @param eleName
	 * @throws IOException
	 */
	public static void waitForToastMsg(WebElement element, IOSDriver driver, String eleName, int seconds)
			throws IOException {
		try {
			logger.info("---------Waiting for visibility of Toast Message---------" + element);

			Wait<IOSDriver> wait = new FluentWait<IOSDriver>(driver).withTimeout(seconds, TimeUnit.SECONDS)
					.pollingEvery(250, TimeUnit.MICROSECONDS).ignoring(NoSuchElementException.class);
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(element)) != null);
			logger.info("---------Element is visible---------" + element);
		} catch (Exception e) {

		}
	}

	/**
	 * Description: This method helps to verify whether given web Element is
	 * present page or not.
	 * 
	 * @param element
	 * @param driver
	 * @param elementName
	 * @throws IOException
	 */
	public static void isEleDisplayed(WebElement element, IOSDriver driver, String elementName) throws IOException {
		try {
			logger.info("---------Verifying element is displayed or not ---------");

			Wait<IOSDriver> wait = new FluentWait<IOSDriver>(driver).withTimeout(1, TimeUnit.MINUTES)
					.pollingEvery(250, TimeUnit.MICROSECONDS).ignoring(NoSuchElementException.class);
			if (element.isDisplayed()) {
				System.out.println(elementName + "------ is displayed");
				MyExtentListners.test.pass("Verify " + "\'" + elementName + "\'" + " is displayed || " + "\'"
						+ elementName + "\'" + " is displayed ");
			}
		} catch (RuntimeException e) {

			MyExtentListners.test.fail(MarkupHelper.createLabel("Verify " + "\'" + elementName + "\'"
					+ " is displayed || " + "\'" + elementName + "\'" + " is not displayed ", ExtentColor.RED));

			MyExtentListners.test.addScreenCaptureFromPath(capture(driver, elementName));
			System.out.println(elementName + "------ is not displayed");
			throw e;
		}
	}

	/**
	 * Description: This method helps to verify whether given web Element is
	 * present page or not.
	 * 
	 * @param element
	 * @param driver
	 * @param elementName
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static boolean isEleDisplayed(WebElement element, IOSDriver driver, int seconds, int loop,String elementName)
			throws IOException, InterruptedException {

		boolean flag = false;

		int count = loop;
		while (count > 0) {
			try {
				logger.info("---------Verifying element is displayed or not ---------");
				count--;
				element.isDisplayed();
				flag = true;
				MyExtentListners.test.pass("Verify " + "\'" + elementName + "\'" + " is displayed || " + "\'"
						+ elementName + "\'" + " is displayed ");
				break;

			} catch (RuntimeException e) {
		
				Thread.sleep(seconds * 1000);
				flag = false;
				MyExtentListners.test.fail("Verify " + "\'" + elementName + "\'" + " is not displayed || " + "\'"
						+ elementName + "\'" + " is not displayed ");
			}
		}
		return flag;
	}

	/**
	 * 
	 * @param element
	 * @param driver
	 * @param elementName
	 * @throws IOException
	 */

	public static void verifyElementIsDisplayed(WebElement element, IOSDriver driver, String elementName)
			throws IOException {
		try {
			logger.info("---------Verifying element is displayed or not ---------");
			Wait<IOSDriver> wait = new FluentWait<IOSDriver>(driver).withTimeout(10, TimeUnit.SECONDS)
					.pollingEvery(250, TimeUnit.MICROSECONDS).ignoring(NoSuchElementException.class);
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(element)) != null);
			MyExtentListners.test.pass("Verify " + "\'" + elementName + "\'" + " is displayed  || " + "\'" + elementName
					+ "\'" + " is displayed ");
		} catch (Exception e) {
			MyExtentListners.test.fail(MarkupHelper.createLabel("Verify " + "\'" + elementName + "\'"
					+ " is displayed  || " + "\'" + elementName + "\'" + " is not displayed ", ExtentColor.RED));

			MyExtentListners.test.addScreenCaptureFromPath(capture(driver, elementName));
			System.out.println(elementName + "------ is not displayed");
			throw e;
		} catch (AssertionError e) {
			MyExtentListners.test.fail(MarkupHelper.createLabel("Verify " + "\'" + elementName + "\'"
					+ " is displayed  || " + "\'" + elementName + "\'" + " is not displayed ", ExtentColor.RED));

			MyExtentListners.test.addScreenCaptureFromPath(capture(driver, elementName));
			System.out.println(elementName + "------ is not displayed");
			throw e;
		}
	}

	
	
	
	
	
	
	/**
	 * Description: this method will click on element which is provided.
	 * 
	 * @param element
	 * @param driver
	 * @param elementName
	 * @throws Exception
	 */
	public static void clickElement(WebElement element, IOSDriver driver, String elementName) throws Exception {

		try {
			logger.info("---------Verifying element is displayed or not ---------");
//			waitForElement(element, driver, elementName, 30);
			element.click();
			MyExtentListners.test.pass("Verify user is able to click on " + "\'" + elementName + "\'"
					+ " ||  User is able to click on " + "\'" + elementName + "\'");
		} catch (AssertionError error) {
			MyExtentListners.test.fail(MarkupHelper.createLabel("Verify user is able to click on " + "\'" + elementName
					+ "\'" + "  || User is not able to click on " + "\'" + elementName + "\'", ExtentColor.RED));

			MyExtentListners.test.addScreenCaptureFromPath(capture(driver, elementName));
			Assert.fail("unable to Click on " + "\'" + elementName + "\'");

			MyExtentListners.test.addScreenCaptureFromPath(capture(driver, elementName));
			throw error;
		} catch (Exception error) {
			MyExtentListners.test.fail(MarkupHelper.createLabel("Verify user is able to click on " + "\'" + elementName
					+ "\'" + " || User is not able to click on " + "\'" + elementName + "\'", ExtentColor.RED));

			MyExtentListners.test.addScreenCaptureFromPath(capture(driver, elementName));
			Assert.fail("unable to Click on " + "\'" + elementName + "\'");
			throw error;
		}

	}

	/**
	 * Description: this method hides keyboard
	 * 
	 * @param driver
	 */

	public static void hideKeyboard(IOSDriver driver) {
		try {
			driver.hideKeyboard();
			Thread.sleep(2000);
		} catch (Throwable e) {

		}
	}

	/**
	 * Description: this method clear texts from textbox/edit box and type the
	 * value which is provided
	 * 
	 * @param element
	 * @param value
	 * @param elementName
	 * @param driver
	 * @throws Exception
	 */
	public static void clearAndType(WebElement element, String value, String elementName, IOSDriver driver)
			throws Exception {
		try {
			logger.info("---------Method clear and type  ---------");
			element.clear();
			logger.info(elementName + " is cleared");
			element.sendKeys(value);
			logger.info(value + " is entered in " + elementName);
			hideKeyboard(driver);
			logger.info(" hide keyboard");
			MyExtentListners.test.pass("Verify user is able to type " + "\'" + value + "\'" + "in " + "\'" + elementName
					+ "\'" + " || User is able to type " + "\'" + value + "\'" + "in " + "\'" + elementName + "\'");
		} catch (AssertionError error) {

			MyExtentListners.test.fail(MarkupHelper.createLabel(
					"Verify user is able to type " + "\'" + value + "\'" + "in " + "\'" + elementName + "\'"
							+ " || User is not able to type " + "\'" + value + "\'" + "in " + "\'" + elementName + "\'",
					ExtentColor.RED));

			MyExtentListners.test.addScreenCaptureFromPath(capture(driver, elementName));
			Assert.fail("Unable to type on " + "\'" + elementName + "\'");
		} catch (Exception e) {
			MyExtentListners.test.fail(MarkupHelper.createLabel(
					"Verify user is able to type " + "\'" + value + "\'" + "in " + "\'" + elementName + "\'"
							+ " || User is not able to type " + "\'" + value + "\'" + "in " + "\'" + elementName + "\'",
					ExtentColor.RED));

			MyExtentListners.test.addScreenCaptureFromPath(capture(driver, elementName));
			Assert.fail("Unable to type in " + "\'" + elementName + "\'");
		}

	}

	/**
	 * Description: this method type the value which is provided
	 * 
	 * @param element
	 * @param value
	 * @param elementName
	 * @param driver
	 * @throws Exception
	 */
	public static void type(WebElement element, String value, String elementName, IOSDriver driver) throws Exception {
		try {
			logger.info("---------Method type  ---------");
			// Thread.sleep(100);
			element.sendKeys(value);
			// Thread.sleep(300);
			hideKeyboard(driver);
			logger.info("---------hide keyboard  ---------");
			MyExtentListners.test.pass("Verify user is able to type " + "\'" + value + "\'" + "in " + "\'" + elementName
					+ "\'" + " || User is able to type " + "\'" + value + "\'" + "in " + "\'" + elementName + "\'");
		} catch (AssertionError error) {
			MyExtentListners.test.fail(MarkupHelper.createLabel(
					"Verify user is able to type " + "\'" + value + "\'" + "in " + "\'" + elementName + "\'"
							+ " || User is not able to type " + "\'" + value + "\'" + "in " + "\'" + elementName + "\'",
					ExtentColor.RED));

			MyExtentListners.test.addScreenCaptureFromPath(capture(driver, elementName));
			Assert.fail("Unable to type on " + elementName);
		} catch (Exception e) {
			MyExtentListners.test.fail(MarkupHelper.createLabel(
					"Verify user is able to type " + "\'" + value + "\'" + "in " + "\'" + elementName + "\'"
							+ " || User is not able to type " + "\'" + value + "\'" + "in " + "\'" + elementName + "\'",
					ExtentColor.RED));

			MyExtentListners.test.addScreenCaptureFromPath(capture(driver, elementName));
			Assert.fail("Unable to type in " + elementName);
		}

	}

	/**
	 * Description:Explicit wait to check element is clickable
	 * 
	 * @param element
	 * @param driver
	 * @param eleName
	 * @throws IOException
	 */
	public static void isEleClickable(WebElement element, IOSDriver driver, String eleName) throws IOException {
		try {
			logger.info("---------Method is Element clickable  ---------");
			Wait<IOSDriver> wait = new FluentWait<IOSDriver>(driver).withTimeout(1, TimeUnit.MINUTES)
					.pollingEvery(250, TimeUnit.MICROSECONDS).ignoring(NoSuchElementException.class);
			Assert.assertTrue(wait.until(ExpectedConditions.elementToBeClickable(element)) != null);
			
			System.out.println(" element is clickable ");
		} catch (AssertionError e) {
			MyExtentListners.test.fail(MarkupHelper.createLabel("Verify " + "\'" + eleName + "\'" + " is clickable || "
					+ "\'" + eleName + "\'" + " is not clickable", ExtentColor.RED));

			MyExtentListners.test.addScreenCaptureFromPath(capture(driver, eleName));
			System.out.println(" element is not clickable ");
			throw e;
		} catch (Exception e) {
			MyExtentListners.test.fail(MarkupHelper.createLabel("Verify " + "\'" + eleName + "\'" + " is clickable || "
					+ "\'" + eleName + "\'" + " is not clickable", ExtentColor.RED));

			MyExtentListners.test.addScreenCaptureFromPath(capture(driver, eleName));
			System.out.println(" element is not clickable ");
			throw e;
		}
	}

	/**
	 * Description: wait till page load until progress bar is invisible
	 * 
	 * @param eleName
	 * @param driver
	 * @param pageName
	 * @throws IOException
	 */

	public static void waitTillPageLoad(String eleName, IOSDriver driver, String pageName, int seconds)
			throws IOException {
		try {
			logger.info("---------Method waiting for invisibility of progress bar  ---------");
			Wait<IOSDriver> wait = new FluentWait<IOSDriver>(driver).withTimeout(seconds, TimeUnit.MINUTES)
					.pollingEvery(250, TimeUnit.MICROSECONDS).ignoring(NoSuchElementException.class);
			Assert.assertTrue(
					(wait.until(ExpectedConditions
							.invisibilityOfElementLocated(By.className("android.widget.ProgressBar")))),
					"On clicking" + eleName + " Page is on load, Unable to proceed");
			MyExtentListners.test.pass(" Verify On clicking " + "\'" + eleName + "\''" + " user is redirected to "
					+ "\'" + pageName + "\''" + "  ||  On clicking " + "\'" + eleName + "\''"
					+ " user is redirected to " + "\'" + pageName + "\''");
		} catch (AssertionError e) {
			MyExtentListners.test.fail(MarkupHelper.createLabel(" Verify On clicking " + "\'" + eleName + "\''"
					+ " user is redirected to " + "\'" + pageName + "\''" + "  ||  On clicking " + "\'" + eleName
					+ "\''" + " user is not redirected to " + "\'" + pageName + "\''", ExtentColor.RED));

			MyExtentListners.test.addScreenCaptureFromPath(capture(driver, eleName));
			Assert.fail("On clicking " + "\'" + eleName + "\''" + ", Page is on load, Unable to proceed");
			throw e;
		} catch (Exception e) {
			MyExtentListners.test.fail(MarkupHelper.createLabel(" Verify On clicking " + "\'" + eleName + "\''"
					+ " user is redirected to " + "\'" + pageName + "\''" + "  ||  On clicking " + "\'" + eleName
					+ "\''" + " user is not redirected to " + "\'" + pageName + "\''", ExtentColor.RED));

			MyExtentListners.test.addScreenCaptureFromPath(capture(driver, eleName));
			Assert.fail("On clicking " + "\'" + eleName + "\''" + ", Page is on load, Unable to proceed");
			throw e;
		}
	}

	/**
	 * Description: Fetch text from element and return as string
	 * 
	 * @param elename
	 * @param driver
	 * @param elementName
	 * @return
	 * @throws IOException
	 */

	public static String gettext(WebElement elename, IOSDriver driver, String elementName) throws IOException {
		logger.info("--------- get text from element  ---------");
		String eleText = null;
		try {
			isEleDisplayed(elename, driver, elementName);
			eleText = elename.getText();
		} catch (Exception e) {

			MyExtentListners.test.addScreenCaptureFromPath(capture(driver, elementName)); // exception
			MyExtentListners.test.addScreenCaptureFromPath(MobileActionUtil.capture(driver, elementName));
			Assert.fail("Unable to fetch text from " + "\'" + elename + "\'");

		}
		return eleText;
	}

	/**
	 * Description: This method verify expected result contains in actual result
	 * 
	 * @param actResult
	 * @param expResult
	 * @throws IOException
	 */

	public static void verifyContainsText(String actResult, String expResult, IOSDriver driver) throws IOException {
		if (actResult.contains(expResult)) {
			MyExtentListners.test.pass("Verify  Expected : " + "\'" + expResult + "\''" + " contains  Actual :  "
					+ actResult + "  || Expected : " + "\'" + expResult + "\''" + "contains  Actual :  " + actResult);

		} else {
			MyExtentListners.test.fail(MarkupHelper.createLabel("Verify  Expected : " + "\'" + expResult + "\''"
					+ " contains  Actual :  " + actResult + " ||  Expected : " + "\'" + expResult + "\''"
					+ " does not contains  Actual :  " + actResult, ExtentColor.RED));
			MyExtentListners.test.addScreenCaptureFromPath(capture(driver, expResult));

		}
	}

	/**
	 * 
	 * @param actResult
	 * @param expResult
	 * @param desc
	 */

	/*
	 * @author:Srinivas Hippargi
	 * 
	 * Description: This method verify expected result contains in actual result
	 */

	public static void verifyContainsText(String actResult, String expResult, String desc) throws Exception {
		if (actResult.contains(expResult)) {
			MyExtentListners.test.pass("Verify  Expected : " + "\'" + expResult + "\''" + " contains  Actual :  "
					+ actResult + "  || Expected : " + "\'" + expResult + "\''" + "contains  Actual :  " + actResult);

		} else {

			MyExtentListners.test.fail(MarkupHelper.createLabel("Verify  Expected : " + "\'" + expResult + "\''"
					+ " contains  Actual :  " + actResult + " ||  Expected : " + "\'" + expResult + "\''"
					+ " does not contains  Actual :  " + actResult, ExtentColor.RED));

			throw new Exception();

		}
	}

	/**
	 * Description: This method verify expected result equals in actual result
	 * 
	 * @param desc
	 * @param actResult
	 * @param expResult
	 */

	/*
	 * @author:Srinivas Hippargi
	 * 
	 * Description: This method verify expected result equals in actual result
	 */

	public static void verifyEqualsText(String desc, String actResult, String expResult) throws Exception {
		if (expResult.equalsIgnoreCase(actResult)) {
			MyExtentListners.test.pass("Verify " + desc + " ||  Expected : " + "\'" + expResult + "\''"
					+ " eqauls  to Actual :  " + actResult);
		} else {
			MyExtentListners.test.fail(MarkupHelper.createLabel("Verify " + desc + "  || Expected : " + "\'" + expResult
					+ "\''" + " not eqauls to  Actual :  " + "\'" + actResult + "\'", ExtentColor.RED));
			throw new Exception();
		}
	}

	/**
	 * Description: This method verify expected result not equals in actual
	 * result
	 * 
	 * @param desc
	 * @param actResult
	 * @param expResult
	 */

	public static void verifyNotEqualsText(String desc, String actResult, String expResult) {
		if (!(expResult.equalsIgnoreCase(actResult))) {
			MyExtentListners.test.pass("Verify " + desc + " is printed on receipt or not" + " ||  Expected : " + "\'"
					+ expResult + "\''" + " not  to Actual :  " + actResult);
		} else {
			MyExtentListners.test
					.fail(MarkupHelper
							.createLabel(
									"Verify " + desc + " is printed on receipt or not" + "  || Expected : " + "\'"
											+ expResult + "\''" + "  eqauls to  Actual :  " + "\'" + actResult + "\'",
									ExtentColor.RED));
		}
	}

	/**
	 * Description: This method verify actual result is not null
	 * 
	 * @param desc
	 * @param actResult
	 * @param expResult
	 */

	public static void verifyIsNull(String actResult, String desc) {
		if (actResult == null) {
			MyExtentListners.test.fail(MarkupHelper.createLabel("Value is null", ExtentColor.RED));
			throw new RuntimeException();
		} else {
			MyExtentListners.test.pass("Verify" + desc + " is Printed on Receipt or not" + " || " + desc + " : "
					+ actResult + " is printed on receipt");
		}
	}

	
	/**
	 * @author :vandhitha
	 * Description: This method to used to scroll in up and down directions 
	 * width
	 * 
	 * @param value
	 * @param startX
	 * @param endX
	 * @param driver
	 */
	
	public static void scrollMobile( String dir, IOSDriver driver) throws Exception{
	
		JavascriptExecutor js = (JavascriptExecutor) driver;
		HashMap<String, String> scrollObject = new HashMap<String, String>();
		scrollObject.put("direction", dir);
		js.executeScript("mobile: scroll", scrollObject);

		System.out.println("Scroll was Successful");
	}
	
	/**
	 * @author :vandhitha
	 * Description: This method is to swipe an element in all directions
	 * width
	 * 
	 * @param value
	 * @param startX
	 * @param endX
	 * @param driver
	 */
	
	public static void SwipeMobile( String dir, IOSDriver driver) throws Exception{
	
		JavascriptExecutor js = (JavascriptExecutor) driver;
		HashMap<String, String> swipeObject = new HashMap<String, String>();
		swipeObject.put("direction", dir);
		js.executeScript("mobile: swipe", swipeObject);

		System.out.println("swipe was Successful");
	}
	
	/**
	 * @author :srinivas
	 * Description: This method to scroll left side based on device height and
	 * width
	 * 
	 * @param value
	 * @param startX
	 * @param endX
	 * @param driver
	 * @throws Exception
	 */

	public static void swipeRightToLeft(int value, double startX, double endX, IOSDriver driver) throws Exception {
		try {
			Thread.sleep(1000);
			System.out.println("inside swipe");
			for (int i = 1; i <= value; i++) {
				Dimension dSize = driver.manage().window().getSize();
				int startx = (int) (dSize.width * startX);
				int endx = (int) (dSize.width * endX);
				int starty = dSize.height / 2;
//				driver.swipe(startx, starty, endx, starty, 1000);
		

			}
		} catch (Exception e) {

			MyExtentListners.test.addScreenCaptureFromPath(capture(driver, "SwipeLeft"));
			throw e;
		}
	}

	/**
	 * Description: This method to scroll Right side based on device height and
	 * width
	 * 
	 * @param value
	 * @param startx
	 * @param endx
	 * @param driver
	 * @throws Exception
	 */
	public static void swipeLefToRight(int value, double startx, double endx, IOSDriver driver) throws Exception {
		try {
			Thread.sleep(1000);
			System.out.println("inside swipe");
			for (int i = 1; i <= value; i++) {
				Dimension dSize = driver.manage().window().getSize();
				int startX = (int) (dSize.width * startx);
				int endX = (int) (dSize.width * endx);
				int starty = dSize.height / 2;
//				driver.swipe(startX, starty, endX, starty, 1000);
			}
		} catch (Exception e) {

			MyExtentListners.test.addScreenCaptureFromPath(capture(driver, "SwipeRight"));
			throw e;

		}
	}
	
	
	
	/**
	 * @author vandhitha
	 * Description: This method to swipe based on device height and width
	 * @param driver
	 * @throws Exception
	 */
	public static void swipeByElements (IOSDriver driver, WebElement endElement) {
        int startX = endElement.getLocation().getX() + (endElement.getSize().getWidth() / 2);
        int startY = endElement.getLocation().getY() + (endElement.getSize().getHeight() / 2);
 
        int endX = endElement.getLocation().getX() + (endElement.getSize().getWidth() / 2);
        int endY = endElement.getLocation().getY() + (endElement.getSize().getHeight() / 2);
 
        new TouchAction(driver)
                .press(point(startX,startY))
                .waitAction()
                .moveTo(point(endX, endY))
                .release().perform();
    }

	/**
	 * Description: This method to scroll Up side based on device height and
	 * width
	 * 
	 * @param value
	 * @param driver
	 * @param starty1
	 * @param endy1
	 * @throws Exception
	 */

	public static void swipeBottomToTop(int value, IOSDriver driver, double starty1, double endy1) throws Exception {
		try {
			Thread.sleep(1000);
			System.out.println("inside swipe");
			for (int i = 1; i <= value; i++) {
				Dimension dSize = driver.manage().window().getSize();
				int starty = (int) (dSize.height * starty1);
				int endy = ((int) (dSize.height * endy1));
				int startx = dSize.width / 2;
//				driver.swipe(startx, starty, startx, endy, 1000);
			}
		} catch (Exception e) {

			MyExtentListners.test.addScreenCaptureFromPath(capture(driver, "SwipeUp"));
			throw e;
		}
	}
	
	
	
	

	/**
	 * Description: This method to scroll Bottom side based on device height and
	 * width
	 * 
	 * @param value
	 * @param driver
	 * @param starty1
	 * @param endy1
	 * @throws Exception
	 */
	public static void swipeTopToBottom(int value, IOSDriver driver, double starty1, double endy1) throws Exception {
		try {
			Thread.sleep(1000);

			System.out.println("inside swipe");
			for (int i = 1; i <= value; i++) {
				Dimension dSize = driver.manage().window().getSize();
				int starty = (int) (dSize.height * starty1);
				int endy = (int) (dSize.height * endy1);
				int startx = dSize.width / 2;
//				driver.swipe(startx, starty, startx, endy, 1000);
			}
		} catch (Exception e) {

			MyExtentListners.test.addScreenCaptureFromPath(capture(driver, "SwipeDown"));
			throw e;
		}
	}
	

	

	/**
	 * Description: This method is to long press on element upto 3 seconds then
	 * release
	 * 
	 * @param driver
	 * @param element
	 * @throws IOException
	 */
	public static void performLongPress(IOSDriver driver, WebElement element) throws IOException {

		try {
			TouchAction act1 = new TouchAction((MobileDriver) driver);
//			act1.longPress(element).waitAction(3000).release().perform();
		} catch (Exception e) {

			MyExtentListners.test.addScreenCaptureFromPath(capture(driver, "LongPress"));
			throw e;
		}
	}
	
	
	
	
	
	

	/**
	 * Description: This method is to capture screenshot
	 * 
	 * @param driver
	 * @param elementName 
	 * @return
	 * @throws IOException
	 */

	public static String capture(IOSDriver driver, String elementName) throws IOException {
		File source = driver.getScreenshotAs(OutputType.FILE);
		String screenShotName = null;
		String dest = MyExtentListners.screenShotPath + screenShotName + ".png";
		System.out.println(dest);
		File destination = new File(dest);
		FileUtils.copyFile(source, destination);
		return dest;
	}

	public static String capture(IOSDriver driver) throws IOException {
		File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
		String sDate = sdf.format(date);

		File source = driver.getScreenshotAs(OutputType.FILE);
		String dest = MyExtentListners.screenShotPath + "/ " + sDate + ".png";
		System.out.println(dest);
		File destination = new File(dest);
		FileUtils.copyFile(source, destination);
		return dest;
	}

	/**
	 * 
	 * Description: Method for Scrolling to particular element based on
	 * direction and device size
	 * 
	 * @param maxScroll
	 * @param start
	 * @param end
	 * @param scrollType
	 * @param element
	 * @param driver
	 * @throws Exception
	 */
	public static void scrollToElement(int maxScroll, double start, double end, String scrollType, WebElement element,
			IOSDriver driver) throws Exception {

		while (maxScroll != 0) {
			try {
				if (element.isDisplayed()) {
					maxScroll++;
					break;
				}
			} catch (Exception e) {
				switch (scrollType.toUpperCase()) {
				case ("DOWN"):
					swipeTopToBottom(1, driver, start, end);
					break;

				case ("UP"):
					swipeBottomToTop(1, driver, start, end);
					break;

				case ("LEFT"):
					swipeRightToLeft(1, start, end, driver);
					break;

				case ("RIGHT"):
					swipeLefToRight(1, start, end, driver);
					break;

				default:
					MyExtentListners.test.warning(MarkupHelper.createLabel(" Invalid Swipe type", ExtentColor.AMBER));

					break;

				}

			}
			maxScroll--;
		}
	}

	/**
	 * 
	 * @param driver
	 * @return
	 * @throws IOException
	 */

	public static String getToastMessage(AppiumDriver driver) throws IOException {
		String result = null;
		File scfile = driver.getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(scfile, new File(GenericLib.sDirPath + "/toasts/screen.png"));
		ITesseract instance = new Tesseract();
		try {
			result = instance.doOCR(scfile);
		} catch (TesseractException e) {
		}
		System.out.println("************* Toast message text************** " + result.toString());
		return result;
	}

	/**
	 * Description : Functional Verification
	 * 
	 * @param desc
	 * @param actResult
	 * @param expResult
	 */

	public static void verifyEqualsText_Funct(String desc, String actResult, String expResult) {
		// if (expResult.equalsIgnoreCase(actResult)) {
		if (expResult.equals(actResult)) {
			MyExtentListners.test.pass("Verify " + desc + " is displayed or not " + " ||  Expected : " + "\'"
					+ expResult + "\''" + " eqauls  to Actual :  " + actResult);
		} else {
			MyExtentListners.test.fail(MarkupHelper
					.createLabel("Verify " + desc + " is diaplayed or not" + "  || Expected : " + "\'" + expResult
							+ "\''" + " not eqauls to  Actual :  " + "\'" + actResult + "\'", ExtentColor.RED));
		}
	}

	/**
	 * 
	 * @param actResult
	 * @param expResult
	 * @param desc
	 */

	public static void verifyContainsText_Funct(String actResult, String expResult, String desc) {
		if (actResult.contains(expResult)) {
			MyExtentListners.test.pass("Verify Text" + desc + " is displayed or not " + " ||  Expected : " + "\'"
					+ expResult + "\''" + " eqauls  to Actual :  " + actResult);

		} else {
			MyExtentListners.test.fail(MarkupHelper
					.createLabel("Verify Text" + desc + " is diaplayed or not" + "  || Expected : " + "\'" + expResult
							+ "\''" + " not eqauls to  Actual :  " + "\'" + actResult + "\'", ExtentColor.RED));

		}
	}

	public static void isEleIsEnabled(WebElement element, IOSDriver driver, String elementName) throws IOException {
		try {
			logger.info("---------Verifying element is Enabled or not ---------");

			Wait<IOSDriver> wait = new FluentWait<IOSDriver>(driver).withTimeout(1, TimeUnit.MINUTES)
					.pollingEvery(250, TimeUnit.MICROSECONDS).ignoring(NoSuchElementException.class);
			if (element.isEnabled()) {
				System.out.println(elementName + "------ is displayed");
				MyExtentListners.test.pass("Verify " + "\'" + elementName + "\'" + " is enabled || " + "\'"
						+ elementName + "\'" + " is enabled ");
			}
		} catch (RuntimeException e) {

			MyExtentListners.test.fail(MarkupHelper.createLabel("Verify " + "\'" + elementName + "\'"
					+ " is enabled || " + "\'" + elementName + "\'" + " is not enabled ", ExtentColor.RED));

			MyExtentListners.test.addScreenCaptureFromPath(capture(driver, elementName));
			System.out.println(elementName + "------ is not Enabled");
			throw e;
		}
	}

	public static void isEleIsSelected_funct(WebElement element, IOSDriver driver, String elementName)
			throws IOException {
		try {
			logger.info("---------Verifying element is Selected or not ---------");

			Wait<IOSDriver> wait = new FluentWait<IOSDriver>(driver).withTimeout(1, TimeUnit.MINUTES)
					.pollingEvery(250, TimeUnit.MICROSECONDS).ignoring(NoSuchElementException.class);
			if (element.isSelected() == false) {
				System.out.println(elementName + "------ is  Not Selected");
				MyExtentListners.test.pass("Verify " + "\'" + elementName + "\'" + " is Not Selected || " + "\'"
						+ elementName + "\'" + " is Not Selected ");
			}
		} catch (RuntimeException e) {

			MyExtentListners.test.fail(MarkupHelper.createLabel("Verify " + "\'" + elementName + "\'"
					+ " is Selected || " + "\'" + elementName + "\'" + " is  Selected ", ExtentColor.RED));

			MyExtentListners.test.addScreenCaptureFromPath(capture(driver, elementName));
			System.out.println(elementName + "------ is Selected");
			throw e;
		}
	}

	/*
	 * @author:Srinivas Hippargi
	 * 
	 * Description: This method is to move to element and click refresh
	 */

	public static void actionClick(WebElement element, IOSDriver driver, String elementName) throws IOException {

		try {
			Actions action = new Actions(driver);
			action.moveToElement(element).click().build().perform();
			MyExtentListners.test.pass("Verify user is able to click on " + "\'" + elementName + "\'"
					+ " ||  User is able to click on " + "\'" + elementName + "\'");
		} catch (AssertionError error) {
			MyExtentListners.test.fail(MarkupHelper.createLabel("Verify user is able to click on " + "\'" + elementName
					+ "\'" + "  || User is not able to click on " + "\'" + elementName + "\'", ExtentColor.RED));

			MyExtentListners.test.addScreenCaptureFromPath(capture(driver, elementName));
			Assert.fail("unable to Click on " + "\'" + elementName + "\'");

			MyExtentListners.test.addScreenCaptureFromPath(capture(driver, elementName));
			throw error;
		} catch (Exception e) {
			MyExtentListners.test.fail(MarkupHelper.createLabel("Verify user is able to click on " + "\'" + elementName
					+ "\'" + " || User is not able to click on " + "\'" + elementName + "\'", ExtentColor.RED));

			MyExtentListners.test.addScreenCaptureFromPath(capture(driver, elementName));
			throw e;
		}

	}

	/*
	 * @author: Srinivas Hippargi
	 * 
	 * 
	 * Description: Tap on particular element based size co-orinates
	 */
//	public static void tapOnElement(double x, double y, IOSDriver driver) throws InterruptedException {
//		Thread.sleep(5000);
//		Dimension dSize = driver.manage().window().getSize();
//		int sx1 = driver.manage().window().getSize().getWidth();
//		int sx2 = driver.manage().window().getSize().getHeight();
//		int sX = (int) (dSize.width * x);
//		int sY = (int) (dSize.height * y);
//		driver.tap(1, sX, sY, 1); // method is deprecated 
		
	
	
	/*
	 * @author: vandhitha
	 *  
	 * 
	 * Description: Tap by coordinates on the screen 
	 */
	public static void tapByCoordinates (int x,  int y,IOSDriver driver) throws InterruptedException{
		Thread.sleep(5000);
        new TouchAction(driver)
                .tap(point(x,y))
                .waitAction(waitOptions(Duration.ofMillis(2500))).perform();
    }

	

	/*
	 * @author:Srinivas Hippargi
	 * 
	 * Description: handles webwiew and native_app mode
	 */
	public static void switchToView(IOSDriver driver) {
		Set<String> contextNames = driver.getContextHandles();
		for (String contextName : contextNames) {
			if (contextName.contains("NATIVE_APP")) {
				driver.context(contextName);
				System.out.println(contextName);
			} else {
				driver.context(contextName);
				System.out.println(contextName);
			}
		}
	}

	/*
	 * @author:Srinivas Hippargi
	 * 
	 * Description: This method is to fetch the system date and time in
	 * yyyy-mm-ddThh-mm-ss
	 * 
	 */
	public static String getSystemDate() {
		SimpleDateFormat dateTimeInGMT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
		Date currentDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		Date finalNewDate = calendar.getTime();
		dateTimeInGMT.setTimeZone(istTimeZone);
		String finalNewDateString = dateTimeInGMT.format(finalNewDate);
		System.out.println(finalNewDateString);
		return finalNewDateString;

	}

	/*
	 * @author:Srinivas Hippargi
	 * 
	 * Description: This method will extract device id (IMEI)
	 * 
	 */
	public static String getDeviceId() throws Exception {

		Process proc = Runtime.getRuntime().exec("adb shell dumpsys iphonesubinfo");
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

		// read the output from the command
		String s = null;
		String deviceId = "";
		while ((s = stdInput.readLine()) != null) {
			if (s.contains("Device ID")) {
				String[] s1 = s.trim().split(" ");
				deviceId = s1[s1.length - 1];
				System.out.println("IMEI-----------" + deviceId);
				break;
			}
		}
		if (deviceId.equals("") || deviceId == null || deviceId.equals(" ")) {

			throw new Exception(" Please connect the device");
		}
		return deviceId.trim();
	}

	/**
	 * Description: wait till page load until progress bar is invisible
	 * 
	 * @param eleName
	 * @param driver
	 * @param pageName
	 * @throws IOException
	 */

	public static void waitTillProgressBarLoad(String eleName, IOSDriver driver, String pageName, int seconds)
			throws IOException {
		try {
			logger.info("---------Method waiting for invisibility of progress bar  ---------");
			Wait<IOSDriver> wait = new FluentWait<IOSDriver>(driver).withTimeout(seconds, TimeUnit.SECONDS)
					.pollingEvery(250, TimeUnit.MICROSECONDS).ignoring(NoSuchElementException.class);
			Assert.assertTrue(
					(wait.until(ExpectedConditions
							.invisibilityOfElementLocated(By.id("com.stellapps.usb:id/progressBar")))),
					"On clicking" + eleName + " Page is on load, Unable to proceed");
			MyExtentListners.test.pass(" Verify On clicking " + "\'" + eleName + "\''" + " user is redirected to "
					+ "\'" + pageName + "\''" + "  ||  On clicking " + "\'" + eleName + "\''"
					+ " user is redirected to " + "\'" + pageName + "\''");
		} catch (AssertionError e) {
			MyExtentListners.test.fail(MarkupHelper.createLabel(" Verify On clicking " + "\'" + eleName + "\''"
					+ " user is redirected to " + "\'" + pageName + "\''" + "  ||  On clicking " + "\'" + eleName
					+ "\''" + " user is not redirected to " + "\'" + pageName + "\''", ExtentColor.RED));

			MyExtentListners.test.addScreenCaptureFromPath(capture(driver, eleName));
			Assert.fail("On clicking " + "\'" + eleName + "\''" + ", Page is on load, Unable to proceed");
			throw e;
		} catch (Exception e) {
			MyExtentListners.test.fail(MarkupHelper.createLabel(" Verify On clicking " + "\'" + eleName + "\''"
					+ " user is redirected to " + "\'" + pageName + "\''" + "  ||  On clicking " + "\'" + eleName
					+ "\''" + " user is not redirected to " + "\'" + pageName + "\''", ExtentColor.RED));

			MyExtentListners.test.addScreenCaptureFromPath(capture(driver, eleName));
			Assert.fail("On clicking " + "\'" + eleName + "\''" + ", Page is on load, Unable to proceed");
			throw e;
		}
	}

	public static String decimalRoundingOff(String data, String format) {

		String str = data;

		switch (format) {

		case ".0":
			if (!(str.contains("."))) {
				str = str + ".0";
			}
			break;

		case ".00":
			if (!(str.contains("."))) {
				str = str + ".00";
			} else if ((str.substring(str.indexOf('.'))).length() == 2) {
				str = str + "0";
			}
			break;
		}

		return str;
	}
	
	
	
	

}
