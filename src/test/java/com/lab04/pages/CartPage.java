package com.lab04.pages;

import com.microsoft.playwright.Page;

public class CartPage extends BasePage {

    private static final String CART_ITEMS = ".cart_item";
    private static final String CART_ITEM_NAME = ".inventory_item_name";
    private static final String CART_ITEM_PRICE = ".inventory_item_price";
    private static final String CHECKOUT_BUTTON = "[data-test='checkout']";
    private static final String CONTINUE_SHOPPING_BUTTON = "[data-test='continue-shopping']";
    private static final String REMOVE_BUTTON = "button[id^='remove']";

    public CartPage(Page page) {
        super(page);
    }

    public boolean isPageDisplayed() {
        return page.url().contains("cart");
    }

    public int getCartItemCount() {
        return page.locator(CART_ITEMS).count();
    }

    public String getFirstItemName() {
        return page.locator(CART_ITEM_NAME).first().textContent();
    }

    public double getFirstItemPrice() {
        String priceText = page.locator(CART_ITEM_PRICE).first().textContent();
        return Double.parseDouble(priceText.replace("$", ""));
    }

    public void removeFirstItem() {
        page.locator(REMOVE_BUTTON).first().click();
    }

    public void proceedToCheckout() {
        page.click(CHECKOUT_BUTTON);
        waitForLoad();
    }

    public void continueShopping() {
        page.click(CONTINUE_SHOPPING_BUTTON);
        waitForLoad();
    }
}
