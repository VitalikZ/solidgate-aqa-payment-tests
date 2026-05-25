package com.solidgate.aqa.fixtures;

import com.solidgate.aqa.util.TestCards;
import org.testng.annotations.DataProvider;

public final class PaymentScenarios {

    private PaymentScenarios() {}

    public static final String[] SUCCESS_ORDER_STATUSES = {"approved", "settle_ok", "auth_ok", "settled"};
    public static final String[] SUCCESS_TX_STATUSES = {"success", "approved", "settled"};

    @DataProvider(name = "successScenarios")
    public static Object[][] successScenarios() {
        return new Object[][]{
                {PaymentScenario.builder()
                        .label("low-amount-USD")
                        .amount(1099)
                        .currency("USD")
                        .card(TestCards.SUCCESS_NON_3DS)
                        .expectedOrderStatuses(SUCCESS_ORDER_STATUSES)
                        .expectedTxStatuses(SUCCESS_TX_STATUSES)
                        .build()},
                {PaymentScenario.builder()
                        .label("higher-amount-EUR")
                        .amount(4999)
                        .currency("EUR")
                        .card(TestCards.SUCCESS_NON_3DS)
                        .expectedOrderStatuses(SUCCESS_ORDER_STATUSES)
                        .expectedTxStatuses(SUCCESS_TX_STATUSES)
                        .build()}
        };
    }
}
