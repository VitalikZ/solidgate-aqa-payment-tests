package com.solidgate.aqa.util;

import lombok.Builder;

@Builder(toBuilder = true)
public record CardData(
        String number,
        String expiryMonth,
        String expiryYear,
        String cvv,
        String cardholderName
) {
    public String maskedNumber() {
        if (number == null || number.length() < 4) return "****";
        return "****" + number.substring(number.length() - 4);
    }
}
