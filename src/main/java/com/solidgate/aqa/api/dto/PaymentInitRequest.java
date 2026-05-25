package com.solidgate.aqa.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PaymentInitRequest(
        @JsonProperty("order") OrderRequest order,
        @JsonProperty("page_customization") PageCustomization pageCustomization
) {
    public static PaymentInitRequest of(OrderRequest order) {
        return PaymentInitRequest.builder()
                .order(order)
                .pageCustomization(PageCustomization.defaultCustomization())
                .build();
    }
}
