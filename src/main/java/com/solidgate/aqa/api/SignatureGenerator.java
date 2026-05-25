package com.solidgate.aqa.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HexFormat;

@Slf4j
@RequiredArgsConstructor
public class SignatureGenerator {

    private static final String HMAC_SHA512 = "HmacSHA512";

    private final String publicKey;
    private final String secretKey;

    public String sign(String jsonBody) {
        String payload = publicKey + jsonBody + publicKey;
        byte[] hmacBytes = hmacSha512(payload.getBytes(StandardCharsets.UTF_8),
                secretKey.getBytes(StandardCharsets.UTF_8));
        String hex = HexFormat.of().formatHex(hmacBytes);
        return Base64.getEncoder().encodeToString(hex.getBytes(StandardCharsets.UTF_8));
    }

    private static byte[] hmacSha512(byte[] data, byte[] key) {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA512);
            mac.init(new SecretKeySpec(key, HMAC_SHA512));
            return mac.doFinal(data);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to compute HMAC-SHA512", e);
        }
    }
}
