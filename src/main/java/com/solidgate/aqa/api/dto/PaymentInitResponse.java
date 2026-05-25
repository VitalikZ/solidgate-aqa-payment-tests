package com.solidgate.aqa.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PaymentInitResponse(
        @JsonProperty("url") String url,
        @JsonProperty("payment_intent") String paymentIntent,
        @JsonProperty("page_id") String pageId
) {}
