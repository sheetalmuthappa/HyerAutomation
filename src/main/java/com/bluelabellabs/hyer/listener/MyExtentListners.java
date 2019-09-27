package com.bluelabellabs.hyer.listener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.bluelabellabs.hyer.library.BaseLib;
import com.bluelabellabs.hyer.library.ExcelLibrary;
import com.bluelabellabs.hyer.library.GenericLib;
import com.bluelabellabs.util.MobileActionUtil;


public class MyExtentListners implements ITestListener {
	public static String[] sDataGuest = null;
	public static ExtentHtmlReporter htmlReporter;
	public static ExtentReports extent;
	public static ExtentTest test;
	public static ThreadLocal parentTest = new ThreadLocal();
	int count = 1;
	public static String sExcelDate;
	public static String sExcelPath;
	public static String screenShotPath;
	public static int iPassCount = 0;
	public static int iFailCount = 0;
	public static int iSkippedCount = 0;
	public static int iTotal_Executed;
	public static String sStartTime;
	public static String sEndTime;
	public static long lTotalExecutionTime;
	public static ArrayList<String> sStatus = new ArrayList<String>();
	public static ArrayList<String> sStartMethodTime = new ArrayList<String>();
	public static ArrayList<String> sMethodEndTime = new ArrayList<String>();

	public static long startTime;
	public static long endtTime;
	String className;

	/*
	 * @author:Srinivas Hippargi
	 * 
	 * Description: On Test Starts Create a report based on @Test Method Name
	 */
	public void onTestStart(ITestResult result) {
		// test = extent.createTest(result.getName().toString());
	className = result.getTestClass().getName().toString();
	className= result.getMethod().getMethodName().toString();
		className = className.substring(className.lastIndexOf(".") + 1, className.length());
		test = extent.createTest(className);
		startTime = result.getStartMillis();
		sStartMethodTime.add(startTime + "");
		parentTest.set(test);
		test.info(className + " has started");
	}

	/*
	 * @author:Srinivas Hippargi
	 * 
	 * Description: On Test Success Write Status Passed to Extent Report
	 */
	public void onTestSuccess(ITestResult result) {
		// extent.createTest(className).assignCategory("Regression").pass("className" +
		// " Has Passed");
		endtTime = result.getEndMillis();
		long totTimeInMethod = endtTime - startTime;
		int seconds = (int) (totTimeInMethod / 1000L) % 60;
		int minutes = (int) (totTimeInMethod / 60000L % 60L);
		int hours = (int) (totTimeInMethod / 3600000L % 24L);

		sStatus.add("Passed");
		test.pass(MarkupHelper.createLabel(className + " case has PASSED", ExtentColor.GREEN));
		try {
			extent.flush();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/*
	 * @author:Srinivas Hippargi
	 * 
	 * Description: On Test Failure Write Status Failed to Extent Report With Label
	 * RED
	 */
	public void onTestFailure(ITestResult result) {
		sStatus.add("Failed");
		
		test.fail(MarkupHelper.createLabel(className + " test script has FAILED", ExtentColor.RED));
		//E.fail(result.getThrowable().getMessage());
		test.fail(result.getThrowable());
		//test.fail(result.getThrowable().getStackTrace().toString());
		try {
			MyExtentListners.test.addScreenCaptureFromPath(MobileActionUtil.capture(BaseLib.gv.iDriver));

			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			extent.flush();
		} catch (Exception e) {
			
			
		
		}
	}

	/*
	 * @author:Srinivas Hippargi
	 * 
	 * Description: On Test Skipped Write Status Skipped to Extent Report With Label
	 * YELLOW
	 */
	public void onTestSkipped(ITestResult result) {
		sStatus.add("Skipped");
		test.skip(MarkupHelper.createLabel(className + " test script has SKIPPED", ExtentColor.YELLOW));
		try {
			extent.flush();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub

	}

	/*
	 * @author:Srinivas Hippargi
	 * 
	 * Description: On Suite Starts 1. Create Directories for Extent Reports, Excel
	 * Report and Screenshots based on system time 2. Add Execution Info Based
	 * Execution Type
	 */

	public void onStart(ITestContext context) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
		String sDate = sdf.format(date);
		sExcelDate = sDate;
		sStartTime = sDate;
		String testOutputDir = GenericLib.sDirPath + "/test-output";
		String htmlDir = GenericLib.sDirPath + "/reports" + "/Run-" + sDate + "/extent/";
		String excelDir = GenericLib.sDirPath + "/reports" + "/Run-" + sDate + "/excel/";
		sExcelPath = excelDir;
		screenShotPath = GenericLib.sDirPath + "/reports" + "/Run-" + sDate + "/screenshots/";
		// String pdfDir = GenericLib.sDirPath + "/reports" + "/Run-" + sDate +
		// "/pdf/";
		// String pdfDir = GenericLib.sDirPath + "/reports/pdfreports";
		System.out.println(testOutputDir);
		System.out.println(htmlDir);

		// Setting test-output folder location
		File testOutputFile = new File(testOutputDir);

		if (!testOutputFile.exists()) {
			System.out.println(testOutputFile + " does not exist");
			return;
		}
		// deletes the test output folder
		if (testOutputFile.isDirectory()) {
			try {
				testOutputFile.delete();
				System.out.println("------test output dir deleted--------");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("Deleted file/folder: " + testOutputFile.getAbsolutePath());

		// Setting Extent Report Location
		File file = new File(htmlDir);
		if (!(file.exists())) {
			file.mkdirs();
			System.out.println("--extent folder created");
		}
		String filepath = htmlDir + "extent_" + sDate + ".html";
		System.out.println(filepath);
		File file1 = new File(filepath);
		if (!(file1.exists())) {
			try {
				file1.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Setting Excel Report Location
		File file2 = new File(excelDir);
		if (!(file2.exists())) {
			file2.mkdirs();
			System.out.println("--excel folder created");
		}

		// Setting ScreenShot Report Location
		File file3 = new File(screenShotPath);
		if (!(file3.exists())) {
			file3.mkdirs();
			System.out.println("--scrrenshot folder created");
		}

		htmlReporter = new ExtentHtmlReporter(file1);
		
		// htmlReporter.setAppendExisting(true);

		extent = ExtentManager.createInstance(file.toString());

		extent.attachReporter(htmlReporter);

		int rowCount = ExcelLibrary.getExcelRowCount(GenericLib.sConfigPath, "config");
		System.out.println(" Total Row Count ============> " + rowCount);
		ArrayList<String> deviceCount = new ArrayList<String>();
		int runStatus = GenericLib.getHeaderColumnIndex(GenericLib.sConfigPath, "config", "Run Status");
		for (int i = 1; i <= rowCount; i++) {

			if (ExcelLibrary.getExcelData(GenericLib.sConfigPath, "config", i, runStatus).equalsIgnoreCase("Yes")) {
				// System.out.println(i);
				deviceCount.add(ExcelLibrary.getExcelData(GenericLib.sConfigPath, "config", i, runStatus));
			}
		}
		System.out.println(deviceCount.size());

		if (String.valueOf(deviceCount.size()).equalsIgnoreCase("1")) {
			for (int i = 1; i <= rowCount; i++) {

				if (ExcelLibrary.getExcelData(GenericLib.sConfigPath, "config", i, runStatus).equalsIgnoreCase("Yes")) {

					int udid = GenericLib.getHeaderColumnIndex(GenericLib.sConfigPath, "config", "Device UDID");
					int devName = GenericLib.getHeaderColumnIndex(GenericLib.sConfigPath, "config", "Device Name");
					int devVersion = GenericLib.getHeaderColumnIndex(GenericLib.sConfigPath, "config",
							"Device Version");
					int xcodeOrgId = GenericLib.getHeaderColumnIndex(GenericLib.sConfigPath, "config", "Xcode_Org_Id");
					extent.setSystemInfo("Application Type", "MOBILE_IOS");
					extent.setSystemInfo("Application Name", "HowUDish");
					extent.setSystemInfo("Test Type", context.getName());
					extent.setSystemInfo("Device Name",
							ExcelLibrary.getExcelData(GenericLib.sConfigPath, "config", i, devName).trim());
					extent.setSystemInfo("IOS Version",
							ExcelLibrary.getExcelData(GenericLib.sConfigPath, "config", i, devVersion).trim());
					extent.setSystemInfo("Device UDID",
							ExcelLibrary.getExcelData(GenericLib.sConfigPath, "config", i, udid).trim());
					extent.setSystemInfo("XcodeOrgId",
							ExcelLibrary.getExcelData(GenericLib.sConfigPath, "config", i,xcodeOrgId ).trim());
					
				}
			}
		} else {

			System.out.println("************PLEASE SELECT ONE DEVICE IN CONFIG******************");
		}
		// Customising the html report view
		htmlReporter.config().getChartVisibilityOnOpen();
		htmlReporter.config().setDocumentTitle("HowUDish Automation");
		htmlReporter.config().setReportName("HowUDish Automation Test Report");
		htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
		htmlReporter.config().setTheme(Theme.DARK);
		htmlReporter.setAppendExisting(true);
		htmlReporter.getExceptionContextInfo();	
	}

	public void onFinish(ITestContext context) {
		extent.flush();
		// flushing all logs and details into the report.
			try {
			iPassCount = context.getPassedTests().size();
			iFailCount = context.getFailedTests().size();
			iSkippedCount = context.getSkippedTests().size();
			iTotal_Executed = iPassCount + iFailCount + iSkippedCount;
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
			String sDate = sdf.format(date);
			sEndTime = sDate;
			lTotalExecutionTime = context.getEndDate().getTime() - context.getStartDate().getTime();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


}
