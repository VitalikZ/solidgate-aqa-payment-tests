package com.solidgate.aqa.fixtures;

import com.solidgate.aqa.util.CardData;
import lombok.Builder;

@Builder
public record PaymentScenario(
        String label,
        long amount,
        String currency,
        CardData card,
        String[] expectedOrderStatuses,
        String[] expectedTxStatuses
) {
    @Override
    public String toString() {
        return label + " (" + amount + " " + currency + ")";
    }
}
