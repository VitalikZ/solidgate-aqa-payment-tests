package com.solidgate.aqa.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.solidgate.aqa.util.CardData;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class PaymentPage {

    private static final Duration FORM_TIMEOUT = Duration.ofSeconds(20);

    private final SelenideElement cardNumberInput = $(byAttribute("data-testid", "cardNumber"));
    private final SelenideElement expiryInput = $(byAttribute("data-testid", "cardExpiryDate"));
    private final SelenideElement cvvInput = $(byAttribute("data-testid", "cardCvv"));
    private final SelenideElement submitButton = $(byAttribute("data-testid", "submit"));

    @Step("Open Payment Page: {url}")
    public PaymentPage openUrl(String url) {
        open(url);
        return this;
    }

    @Step("Fill card details")
    public PaymentPage fillCard(CardData card) {
        cardNumberInput.shouldBe(Condition.visible, FORM_TIMEOUT).setValue(card.number());
        expiryInput.shouldBe(Condition.visible).setValue(card.expiryMonth() + card.expiryYear());
        cvvInput.shouldBe(Condition.visible).setValue(card.cvv());
        return this;
    }

    @Step("Submit payment")
    public SuccessPage submit() {
        submitButton.shouldBe(Condition.enabled, FORM_TIMEOUT).click();
        return new SuccessPage();
    }
}
