package com.solidgate.aqa.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record OrderRequest(
        @JsonProperty("order_id") String orderId,
        @JsonProperty("amount") long amount,
        @JsonProperty("currency") String currency,
        @JsonProperty("order_description") String orderDescription,
        @JsonProperty("order_items") String orderItems,
        @JsonProperty("type") String type,
        @JsonProperty("customer_email") String customerEmail,
        @JsonProperty("ip_address") String ipAddress,
        @JsonProperty("platform") String platform,
        @JsonProperty("language") String language,
        @JsonProperty("success_url") String successUrl,
        @JsonProperty("fail_url") String failUrl
) {
    public static OrderRequest sampleSuccessOrder(String orderId, long amount, String currency) {
        return OrderRequest.builder()
                .orderId(orderId)
                .amount(amount)
                .currency(currency)
                .orderDescription("AQA test payment " + orderId)
                .orderItems("AQA test item")
                .customerEmail("aqa-test@example.com")
                .ipAddress("8.8.8.8")
                .platform("WEB")
                .language("en")
                .build();
    }
}
