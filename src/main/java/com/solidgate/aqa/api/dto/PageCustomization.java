package com.solidgate.aqa.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PageCustomization(
        @JsonProperty("public_name") String publicName,
        @JsonProperty("order_title") String orderTitle,
        @JsonProperty("order_description") String orderDescription,
        @JsonProperty("payment_methods") List<String> paymentMethods,
        @JsonProperty("button_color") String buttonColor
) {
    public static PageCustomization defaultCustomization() {
        return PageCustomization.builder()
                .publicName("AQA Test Store")
                .orderTitle("AQA Test Order")
                .orderDescription("Automated test payment")
                .build();
    }
}
