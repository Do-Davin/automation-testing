package com.lab04.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;

import java.nio.file.Paths;

public class BasePage {

    protected final Page page;

    public BasePage(Page page) {
        this.page = page;
    }

    public void waitForLoad() {
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    public String getTitle() {
        return page.title();
    }

    public byte[] takeScreenshot() {
        return page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
    }

    public void takeScreenshot(String filePath) {
        page.screenshot(new Page.ScreenshotOptions()
                .setFullPage(true)
                .setPath(Paths.get(filePath)));
    }
}
