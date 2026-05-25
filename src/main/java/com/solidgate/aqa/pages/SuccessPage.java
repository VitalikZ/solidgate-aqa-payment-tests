package com.solidgate.aqa.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

public class SuccessPage {

    private static final String SUCCESS_TEXT = "Payment successful";

    private final SelenideElement successMessage = $(withText(SUCCESS_TEXT));

    @Step("Verify success page")
    public SuccessPage assertVisible() {
        successMessage.shouldBe(Condition.visible, Duration.ofSeconds(30));
        return this;
    }
}
