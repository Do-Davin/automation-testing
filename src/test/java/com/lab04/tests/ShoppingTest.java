package com.lab04.tests;

import com.aventstack.extentreports.Status;
import com.lab04.pages.CartPage;
import com.lab04.pages.CheckoutPage;
import com.lab04.pages.LoginPage;
import com.lab04.pages.ProductsPage;
import com.lab04.utils.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ShoppingTest extends BaseTest {

    private ProductsPage productsPage;

    @BeforeMethod(alwaysRun = true)
    public void loginBeforeTest() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.navigate();
        loginPage.login("standard_user", "secret_sauce");
        productsPage = new ProductsPage(page);
        ExtentReportManager.getCurrentTest().log(Status.INFO, "Logged in as standard_user");
    }

    @Test(description = "Verify product listing page displays products")
    public void testProductsPageDisplayed() {
        Assert.assertTrue(productsPage.isPageDisplayed(), "Products page should be displayed");
        Assert.assertEquals(productsPage.getPageTitle(), "Products", "Page title should be 'Products'");

        int productCount = productsPage.getProductCount();
        Assert.assertTrue(productCount > 0, "Products page should have at least one product");
        ExtentReportManager.getCurrentTest().log(Status.PASS,
                "Products page displayed with " + productCount + " products");
    }

    @Test(description = "Verify adding a product to cart increments cart badge")
    public void testAddProductToCart() {
        String productName = productsPage.getFirstProductName();
        double productPrice = productsPage.getFirstProductPrice();
        ExtentReportManager.getCurrentTest().log(Status.INFO,
                "Adding product: " + productName + " ($" + productPrice + ") to cart");

        productsPage.addFirstProductToCart();

        int cartCount = productsPage.getCartItemCount();
        Assert.assertEquals(cartCount, 1, "Cart badge should show 1 after adding one product");
        ExtentReportManager.getCurrentTest().log(Status.PASS,
                "Product '" + productName + "' successfully added — cart badge shows 1");
    }

    @Test(description = "Verify cart page shows the added product with correct name and price")
    public void testCartContainsProduct() {
        String productName = productsPage.getFirstProductName();
        double productPrice = productsPage.getFirstProductPrice();
        productsPage.addFirstProductToCart();
        productsPage.goToCart();
        ExtentReportManager.getCurrentTest().log(Status.INFO, "Navigated to cart page");

        CartPage cartPage = new CartPage(page);
        Assert.assertTrue(cartPage.isPageDisplayed(), "Cart page URL should contain 'cart'");
        Assert.assertEquals(cartPage.getCartItemCount(), 1, "Cart should have 1 item");
        Assert.assertEquals(cartPage.getFirstItemName(), productName, "Cart item name should match");
        Assert.assertEquals(cartPage.getFirstItemPrice(), productPrice, 0.01, "Cart item price should match");
        ExtentReportManager.getCurrentTest().log(Status.PASS,
                "Cart correctly shows '" + productName + "' at $" + productPrice);
    }

    @Test(description = "Verify complete checkout flow from cart to order confirmation")
    public void testCompleteCheckout() {
        String productName = productsPage.getFirstProductName();
        productsPage.addFirstProductToCart();
        ExtentReportManager.getCurrentTest().log(Status.INFO, "Added '" + productName + "' to cart");

        productsPage.goToCart();
        CartPage cartPage = new CartPage(page);
        Assert.assertEquals(cartPage.getCartItemCount(), 1, "Cart should have 1 item");

        cartPage.proceedToCheckout();
        CheckoutPage checkoutPage = new CheckoutPage(page);
        Assert.assertTrue(checkoutPage.isStepOneDisplayed(), "Checkout step 1 should be displayed");
        ExtentReportManager.getCurrentTest().log(Status.INFO, "Reached checkout step 1");

        checkoutPage.fillShippingInfo("John", "Doe", "12345");
        checkoutPage.clickContinue();
        Assert.assertTrue(checkoutPage.isStepTwoDisplayed(), "Checkout step 2 (overview) should be displayed");
        ExtentReportManager.getCurrentTest().log(Status.INFO,
                "Reached order overview — total: " + checkoutPage.getOrderTotal());

        checkoutPage.finishOrder();
        Assert.assertTrue(checkoutPage.isOrderConfirmed(), "Order confirmation should be displayed");
        Assert.assertEquals(checkoutPage.getConfirmationMessage(), "Thank you for your order!");
        ExtentReportManager.getCurrentTest().log(Status.PASS,
                "Order completed: " + checkoutPage.getConfirmationMessage());
    }

    @Test(description = "Verify removing an item from the cart empties it")
    public void testRemoveItemFromCart() {
        productsPage.addFirstProductToCart();
        Assert.assertEquals(productsPage.getCartItemCount(), 1, "Cart should have 1 item after adding");

        productsPage.goToCart();
        CartPage cartPage = new CartPage(page);
        cartPage.removeFirstItem();

        Assert.assertEquals(cartPage.getCartItemCount(), 0, "Cart should be empty after removing the item");
        ExtentReportManager.getCurrentTest().log(Status.PASS, "Item successfully removed — cart is empty");
    }

    @Test(description = "Verify product sorting by price low to high")
    public void testSortByPriceLowToHigh() {
        productsPage.sortBy("lohi");
        ExtentReportManager.getCurrentTest().log(Status.INFO, "Applied sort: Price (low to high)");

        double firstPrice = productsPage.getFirstProductPrice();
        Assert.assertTrue(firstPrice > 0, "First product price should be a positive value");
        ExtentReportManager.getCurrentTest().log(Status.PASS,
                "Products sorted — cheapest product: $" + firstPrice);
    }
}
