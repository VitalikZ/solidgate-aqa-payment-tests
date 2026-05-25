package com.solidgate.aqa.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public final class TestConfig {

    private static final TestConfig INSTANCE = new TestConfig();

    private final String merchantId;
    private final String secretKey;
    private final String apiBaseUrl;
    private final String paymentPageBaseUrl;

    private TestConfig() {
        this.merchantId = required("SOLIDGATE_MERCHANT_ID");
        this.secretKey = required("SOLIDGATE_SECRET_KEY");
        this.apiBaseUrl = optional("SOLIDGATE_API_BASE_URL", "https://pay.solidgate.com/api/v1");
        this.paymentPageBaseUrl = optional("SOLIDGATE_PAYMENT_PAGE_BASE_URL",
                "https://payment-page.solidgate.com/api/v1");
        log.info("Test config loaded: apiBaseUrl={}, paymentPageBaseUrl={}, merchantId={}",
                apiBaseUrl, paymentPageBaseUrl, maskMerchantId(merchantId));
    }

    public static TestConfig get() {
        return INSTANCE;
    }

    private static String required(String key) {
        String value = read(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Required environment variable '" + key + "' is not set. " +
                    "Copy .env.example to .env and fill in the values, or export the variable.");
        }
        return value;
    }

    private static String optional(String key, String fallback) {
        String value = read(key);
        return (value == null || value.isBlank()) ? fallback : value;
    }

    private static String read(String key) {
        String env = System.getenv(key);
        return env != null ? env : System.getProperty(key);
    }

    private static String maskMerchantId(String id) {
        if (id == null || id.length() <= 6) return "***";
        return id.substring(0, 4) + "***" + id.substring(id.length() - 2);
    }
}
