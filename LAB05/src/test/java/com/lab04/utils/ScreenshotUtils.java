package com.lab04.utils;

import com.microsoft.playwright.Page;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtils {

    private static final String SCREENSHOTS_DIR = "test-output/screenshots";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    static {
        try {
            Files.createDirectories(Paths.get(SCREENSHOTS_DIR));
        } catch (IOException e) {
            throw new RuntimeException("Failed to create screenshots directory", e);
        }
    }

    public static byte[] capture(Page page, String testName) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String fileName = String.format("%s/%s_%s.png", SCREENSHOTS_DIR, sanitize(testName), timestamp);
        Path screenshotPath = Paths.get(fileName);
        return page.screenshot(new Page.ScreenshotOptions()
                .setFullPage(true)
                .setPath(screenshotPath));
    }

    public static String captureAndGetPath(Page page, String testName) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String fileName = String.format("%s/%s_%s.png", SCREENSHOTS_DIR, sanitize(testName), timestamp);
        Path screenshotPath = Paths.get(fileName);
        page.screenshot(new Page.ScreenshotOptions()
                .setFullPage(true)
                .setPath(screenshotPath));
        return screenshotPath.toAbsolutePath().toString();
    }

    private static String sanitize(String name) {
        return name.replaceAll("[^a-zA-Z0-9_\\-]", "_");
    }
}
