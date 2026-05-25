package com.solidgate.aqa.browser;

import com.codeborne.selenide.Configuration;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class WebDriverFactory {

    public static void configure(BrowserType browser) {
        Configuration.browser = browser.selenideName();
        Configuration.headless = Boolean.parseBoolean(System.getProperty("headless", "false"));
        Configuration.browserSize = System.getProperty("browserSize", "1920x1080");
        Configuration.timeout = Long.parseLong(System.getProperty("timeout", "10000"));
        Configuration.pageLoadTimeout = Long.parseLong(System.getProperty("pageLoadTimeout", "30000"));
        Configuration.screenshots = true;
        Configuration.savePageSource = false;
        Configuration.reportsFolder = "build/selenide-reports";
        Configuration.downloadsFolder = "build/downloads";

        log.info("Browser configured: {} (headless={}, size={}, timeout={}ms)",
                Configuration.browser, Configuration.headless,
                Configuration.browserSize, Configuration.timeout);
    }
}
