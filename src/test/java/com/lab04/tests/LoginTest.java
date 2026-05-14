package com.lab04.tests;

import com.aventstack.extentreports.Status;
import com.lab04.pages.LoginPage;
import com.lab04.pages.ProductsPage;
import com.lab04.utils.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(description = "Verify successful login with valid credentials")
    public void testValidLogin() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.navigate();
        ExtentReportManager.getCurrentTest().log(Status.INFO, "Navigated to SauceDemo login page");

        loginPage.login("standard_user", "secret_sauce");
        ExtentReportManager.getCurrentTest().log(Status.INFO, "Submitted valid credentials");

        ProductsPage productsPage = new ProductsPage(page);
        Assert.assertTrue(productsPage.isPageDisplayed(), "Products page should be displayed after login");
        Assert.assertEquals(productsPage.getPageTitle(), "Products");
        ExtentReportManager.getCurrentTest().log(Status.PASS, "Successfully logged in — products page visible");
    }

    @Test(description = "Verify login fails with invalid credentials")
    public void testInvalidLogin() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.navigate();
        ExtentReportManager.getCurrentTest().log(Status.INFO, "Navigated to SauceDemo login page");

        loginPage.login("invalid_user", "wrong_password");
        ExtentReportManager.getCurrentTest().log(Status.INFO, "Submitted invalid credentials");

        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error message should be displayed");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username and password do not match"),
                "Error should indicate credential mismatch");
        ExtentReportManager.getCurrentTest().log(Status.PASS, "Error message correctly displayed for invalid credentials");
    }

    @Test(description = "Verify login with empty credentials shows error")
    public void testEmptyCredentials() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.navigate();
        ExtentReportManager.getCurrentTest().log(Status.INFO, "Navigated to SauceDemo login page");

        loginPage.login("", "");
        ExtentReportManager.getCurrentTest().log(Status.INFO, "Submitted empty credentials");

        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error message should be displayed for empty credentials");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username is required"),
                "Error should indicate username is required");
        ExtentReportManager.getCurrentTest().log(Status.PASS, "Error correctly displayed for empty credentials");
    }

    @Test(description = "Verify locked out user cannot login")
    public void testLockedOutUser() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.navigate();
        ExtentReportManager.getCurrentTest().log(Status.INFO, "Navigated to SauceDemo login page");

        loginPage.login("locked_out_user", "secret_sauce");
        ExtentReportManager.getCurrentTest().log(Status.INFO, "Attempted login with locked_out_user");

        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error message should be displayed for locked out user");
        Assert.assertTrue(loginPage.getErrorMessage().contains("locked out"),
                "Error should indicate user is locked out");
        ExtentReportManager.getCurrentTest().log(Status.PASS, "Locked out user correctly denied access");
    }
}
