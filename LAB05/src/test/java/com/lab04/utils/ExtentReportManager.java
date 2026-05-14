package com.lab04.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportManager {

    private static ExtentReports extentReports;
    private static final ThreadLocal<ExtentTest> currentTest = new ThreadLocal<>();
    private static final String REPORT_PATH = "test-output/ExtentReport.html";

    public static synchronized ExtentReports getInstance() {
        if (extentReports == null) {
            extentReports = createInstance();
        }
        return extentReports;
    }

    private static ExtentReports createInstance() {
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(REPORT_PATH);
        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setDocumentTitle("E-Commerce UI Automation Report");
        sparkReporter.config().setReportName("Playwright Test Results");
        sparkReporter.config().setEncoding("UTF-8");

        ExtentReports extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("Application URL", "https://www.saucedemo.com");
        return extent;
    }

    public static void setCurrentTest(ExtentTest test) {
        currentTest.set(test);
    }

    public static ExtentTest getCurrentTest() {
        return currentTest.get();
    }

    public static void flush() {
        if (extentReports != null) {
            extentReports.flush();
        }
    }
}
