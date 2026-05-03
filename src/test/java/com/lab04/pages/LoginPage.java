package com.lab04.pages;

import com.microsoft.playwright.Page;

public class LoginPage extends BasePage {

    private static final String URL = "https://www.saucedemo.com";
    private static final String USERNAME_INPUT = "#user-name";
    private static final String PASSWORD_INPUT = "#password";
    private static final String LOGIN_BUTTON = "#login-button";
    private static final String ERROR_MESSAGE = "[data-test='error']";

    public LoginPage(Page page) {
        super(page);
    }

    public void navigate() {
        page.navigate(URL);
        waitForLoad();
    }

    public void login(String username, String password) {
        page.fill(USERNAME_INPUT, username);
        page.fill(PASSWORD_INPUT, password);
        page.click(LOGIN_BUTTON);
        waitForLoad();
    }

    public boolean isErrorDisplayed() {
        return page.isVisible(ERROR_MESSAGE);
    }

    public String getErrorMessage() {
        return page.textContent(ERROR_MESSAGE);
    }

    public boolean isLoginPageDisplayed() {
        return page.isVisible(LOGIN_BUTTON);
    }
}
