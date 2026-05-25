package com.solidgate.aqa.tests.unit;

import com.solidgate.aqa.api.SignatureGenerator;
import org.testng.annotations.Test;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

public class SignatureGeneratorTest {

    @Test
    public void signatureIsDeterministicHmacSha512InHexBase64() {
        SignatureGenerator gen = new SignatureGenerator("public_key_xyz", "secret_key_abc");
        String body = "{\"order_id\":\"x\"}";

        String first = gen.sign(body);
        assertThat(first).isEqualTo(gen.sign(body));

        String hex = new String(Base64.getDecoder().decode(first));
        assertThat(hex).matches("[0-9a-f]{128}");
    }
}
