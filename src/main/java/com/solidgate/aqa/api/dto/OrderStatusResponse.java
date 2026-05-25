package com.solidgate.aqa.api.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderStatusResponse {

    @JsonProperty("order")
    private Order order;

    @JsonProperty("transactions")
    private Map<String, Transaction> transactions = new HashMap<>();

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Order {
        @JsonProperty("order_id") private String orderId;
        @JsonProperty("amount") private long amount;
        @JsonProperty("currency") private String currency;
        @JsonProperty("status") private String status;
        @JsonProperty("processing_amount") private Long processingAmount;
        @JsonProperty("processing_currency") private String processingCurrency;

        private final Map<String, Object> additional = new HashMap<>();

        @JsonAnySetter
        public void setAdditional(String key, Object value) {
            additional.put(key, value);
        }
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Transaction {
        @JsonProperty("status") private String status;
        @JsonProperty("operation") private String operation;
        @JsonProperty("amount") private long amount;
        @JsonProperty("currency") private String currency;
        @JsonProperty("descriptor") private String descriptor;
    }
}
