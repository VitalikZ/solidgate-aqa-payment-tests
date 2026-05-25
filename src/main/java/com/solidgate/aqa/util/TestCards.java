package com.solidgate.aqa.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TestCards {

    public static final CardData SUCCESS_NON_3DS = CardData.builder()
            .number("4067429974719265")
            .expiryMonth("12")
            .expiryYear("30")
            .cvv("123")
            .cardholderName("AQA Tester")
            .build();
}
