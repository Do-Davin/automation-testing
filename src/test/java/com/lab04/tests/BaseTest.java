package com.lab04.tests;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.lab04.utils.ExtentReportManager;
import com.lab04.utils.ScreenshotUtils;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.util.Base64;

public class BaseTest {

    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeSuite(alwaysRun = true)
    public void initReport() {
        ExtentReportManager.getInstance();
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp(ITestResult result) {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(100));
        context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(1920, 1080));
        page = context.newPage();

        ExtentTest test = ExtentReportManager.getInstance()
                .createTest(result.getMethod().getMethodName(),
                        result.getMethod().getDescription());
        ExtentReportManager.setCurrentTest(test);
        test.log(Status.INFO, "Browser launched: Chromium");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        ExtentTest test = ExtentReportManager.getCurrentTest();
        if (test != null && page != null) {
            try {
                if (result.getStatus() == ITestResult.FAILURE) {
                    byte[] screenshot = ScreenshotUtils.capture(page, result.getMethod().getMethodName());
                    String base64 = Base64.getEncoder().encodeToString(screenshot);
                    test.fail(result.getThrowable(),
                            MediaEntityBuilder.createScreenCaptureFromBase64String(base64, "Failure Screenshot").build());
                } else if (result.getStatus() == ITestResult.SUCCESS) {
                    byte[] screenshot = ScreenshotUtils.capture(page, result.getMethod().getMethodName() + "_pass");
                    String base64 = Base64.getEncoder().encodeToString(screenshot);
                    test.pass("Test passed",
                            MediaEntityBuilder.createScreenCaptureFromBase64String(base64, "Pass Screenshot").build());
                } else if (result.getStatus() == ITestResult.SKIP) {
                    test.skip("Test skipped");
                }
            } catch (Exception e) {
                test.log(Status.WARNING, "Screenshot capture failed: " + e.getMessage());
            }
        }

        if (page != null) page.close();
        if (context != null) context.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

    @AfterSuite(alwaysRun = true)
    public void tearDownReport() {
        ExtentReportManager.flush();
    }
}
