package com.lab04.pages;

import com.microsoft.playwright.Page;

public class CheckoutPage extends BasePage {

    // Step 1 selectors
    private static final String FIRST_NAME = "[data-test='firstName']";
    private static final String LAST_NAME = "[data-test='lastName']";
    private static final String ZIP_CODE = "[data-test='postalCode']";
    private static final String CONTINUE_BUTTON = "[data-test='continue']";

    // Step 2 selectors
    private static final String ITEM_TOTAL = ".summary_subtotal_label";
    private static final String TAX = ".summary_tax_label";
    private static final String TOTAL = ".summary_total_label";
    private static final String FINISH_BUTTON = "[data-test='finish']";

    // Confirmation selectors
    private static final String CONFIRMATION_HEADER = ".complete-header";

    public CheckoutPage(Page page) {
        super(page);
    }

    public boolean isStepOneDisplayed() {
        return page.isVisible(FIRST_NAME);
    }

    public boolean isStepTwoDisplayed() {
        return page.isVisible(FINISH_BUTTON);
    }

    public void fillShippingInfo(String firstName, String lastName, String zipCode) {
        page.fill(FIRST_NAME, firstName);
        page.fill(LAST_NAME, lastName);
        page.fill(ZIP_CODE, zipCode);
    }

    public void clickContinue() {
        page.click(CONTINUE_BUTTON);
        waitForLoad();
    }

    public String getItemTotal() {
        return page.textContent(ITEM_TOTAL);
    }

    public String getTax() {
        return page.textContent(TAX);
    }

    public String getOrderTotal() {
        return page.textContent(TOTAL);
    }

    public void finishOrder() {
        page.click(FINISH_BUTTON);
        waitForLoad();
    }

    public boolean isOrderConfirmed() {
        return page.isVisible(CONFIRMATION_HEADER);
    }

    public String getConfirmationMessage() {
        return page.textContent(CONFIRMATION_HEADER);
    }
}
