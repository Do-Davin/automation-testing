package com.lab04.api.tests;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.lab04.api.config.RequestSpecFactory;
import com.lab04.utils.ExtentReportManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

public class ApiBaseTest {

    protected RequestSpecification requestSpec;

    @BeforeSuite(alwaysRun = true)
    public void initReport() {
        ExtentReportManager.getInstance();
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp(ITestResult result) {
        requestSpec = RestAssured.given().spec(RequestSpecFactory.buildBaseSpec());

        ExtentTest test = ExtentReportManager.getInstance()
                .createTest(
                        "[API] " + result.getMethod().getMethodName(),
                        result.getMethod().getDescription());
        ExtentReportManager.setCurrentTest(test);
        test.log(Status.INFO, "Request spec initialized — base URI: " + requestSpec.toString());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        ExtentTest test = ExtentReportManager.getCurrentTest();
        if (test == null) return;

        if (result.getStatus() == ITestResult.FAILURE) {
            test.fail(result.getThrowable());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.pass("Test passed");
        } else if (result.getStatus() == ITestResult.SKIP) {
            test.skip("Test skipped");
        }
    }

    @AfterSuite(alwaysRun = true)
    public void tearDownReport() {
        ExtentReportManager.flush();
    }

    // -----------------------------------------------------------------------
    // Helper: log request + response detail into the Extent report
    // -----------------------------------------------------------------------
    protected void logResponse(Response response, String label) {
        ExtentTest test = ExtentReportManager.getCurrentTest();
        if (test == null) return;

        String detail = String.format(
                "<pre><b>%s</b>%nStatus : %d%nHeaders: %s%nBody   :%n%s</pre>",
                label,
                response.getStatusCode(),
                response.getHeaders().toString(),
                response.getBody().asPrettyString());
        test.log(Status.INFO, detail);
    }
}
