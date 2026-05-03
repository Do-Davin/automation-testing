package com.lab04.pages;

import com.microsoft.playwright.Page;

public class ProductsPage extends BasePage {

    private static final String PAGE_TITLE = ".title";
    private static final String PRODUCTS_LIST = ".inventory_list";
    private static final String PRODUCT_ITEM = ".inventory_item";
    private static final String CART_BADGE = ".shopping_cart_badge";
    private static final String CART_LINK = ".shopping_cart_link";

    public ProductsPage(Page page) {
        super(page);
    }

    public boolean isPageDisplayed() {
        return page.isVisible(PRODUCTS_LIST);
    }

    public String getPageTitle() {
        return page.textContent(PAGE_TITLE);
    }

    public int getProductCount() {
        return page.locator(PRODUCT_ITEM).count();
    }

    public void addProductToCart(String productName) {
        String addButtonSelector = String.format(
                ".inventory_item:has(.inventory_item_name:text('%s')) button", productName);
        page.click(addButtonSelector);
    }

    public void addFirstProductToCart() {
        page.locator(".inventory_item button").first().click();
    }

    public String getFirstProductName() {
        return page.locator(".inventory_item_name").first().textContent();
    }

    public double getFirstProductPrice() {
        String priceText = page.locator(".inventory_item_price").first().textContent();
        return Double.parseDouble(priceText.replace("$", ""));
    }

    public int getCartItemCount() {
        if (page.isVisible(CART_BADGE)) {
            return Integer.parseInt(page.textContent(CART_BADGE));
        }
        return 0;
    }

    public void goToCart() {
        page.click(CART_LINK);
        waitForLoad();
    }

    public void sortBy(String option) {
        page.selectOption(".product_sort_container", option);
    }
}
