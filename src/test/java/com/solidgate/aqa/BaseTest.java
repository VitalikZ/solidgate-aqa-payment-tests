package com.solidgate.aqa;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.solidgate.aqa.api.SolidgateApiClient;
import com.solidgate.aqa.browser.BrowserType;
import com.solidgate.aqa.browser.WebDriverFactory;
import com.solidgate.aqa.pages.PaymentPage;
import io.qameta.allure.selenide.AllureSelenide;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

public abstract class BaseTest {

    protected SolidgateApiClient api;
    protected PaymentPage paymentPage;

    @BeforeSuite(alwaysRun = true)
    public void configureAllure() {
        SelenideLogger.addListener("AllureSelenide",
                new AllureSelenide()
                        .screenshots(true)
                        .savePageSource(false)
                        .includeSelenideSteps(true));
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        WebDriverFactory.configure(BrowserType.fromSystemProperty());
        api = new SolidgateApiClient();
        paymentPage = new PaymentPage();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        Selenide.closeWebDriver();
    }
}
