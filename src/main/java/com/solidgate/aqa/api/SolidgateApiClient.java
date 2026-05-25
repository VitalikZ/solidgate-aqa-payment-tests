package com.solidgate.aqa.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.solidgate.aqa.api.dto.OrderRequest;
import com.solidgate.aqa.api.dto.OrderStatusResponse;
import com.solidgate.aqa.api.dto.PaymentInitRequest;
import com.solidgate.aqa.api.dto.PaymentInitResponse;
import com.solidgate.aqa.config.TestConfig;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class SolidgateApiClient {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    private final TestConfig config;
    private final SignatureGenerator signature;

    public SolidgateApiClient() {
        this.config = TestConfig.get();
        this.signature = new SignatureGenerator(config.getMerchantId(), config.getSecretKey());
    }

    @Step("Init Payment Page for order {order.orderId}")
    public PaymentInitResponse initPaymentPage(OrderRequest order) {
        String body = toJson(PaymentInitRequest.of(order));
        Response response = signedRequest(body)
                .baseUri(config.getPaymentPageBaseUrl())
                .body(body)
                .post("/init");

        if (response.statusCode() != 200) {
            throw new SolidgateApiException("Failed to init Payment Page",
                    response.statusCode(), response.asString());
        }
        return response.as(PaymentInitResponse.class);
    }

    @Step("Get order status: {orderId}")
    public OrderStatusResponse getOrderStatus(String orderId) {
        String body = toJson(new OrderIdBody(orderId));
        Response response = signedRequest(body)
                .baseUri(config.getApiBaseUrl())
                .body(body)
                .post("/status");

        if (response.statusCode() != 200) {
            throw new SolidgateApiException("Failed to get order status",
                    response.statusCode(), response.asString());
        }
        return response.as(OrderStatusResponse.class);
    }

    @Step("Wait for order status: {orderId}")
    public OrderStatusResponse waitForOrderStatus(String orderId, int timeoutSeconds) {
        long deadline = System.currentTimeMillis() + timeoutSeconds * 1000L;
        SolidgateApiException last = null;
        while (System.currentTimeMillis() < deadline) {
            try {
                OrderStatusResponse status = getOrderStatus(orderId);
                if (status.getOrder() != null) return status;
            } catch (SolidgateApiException e) {
                last = e;
            }
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Interrupted while polling /status", e);
            }
        }
        if (last != null) throw last;
        throw new IllegalStateException("Order " + orderId + " not visible via /status after "
                + timeoutSeconds + "s");
    }

    private RequestSpecification signedRequest(String body) {
        return RestAssured.given()
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Merchant", config.getMerchantId())
                .header("Signature", signature.sign(body));
    }

    private static String toJson(Object value) {
        try {
            return MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize request body", e);
        }
    }

    public static class SolidgateApiException extends RuntimeException {
        public SolidgateApiException(String message, int statusCode, String body) {
            super("%s (HTTP %d): %s".formatted(message, statusCode, body));
        }
    }

    private record OrderIdBody(@JsonProperty("order_id") String orderId) {}
}
