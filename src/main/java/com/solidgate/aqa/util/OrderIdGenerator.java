package com.solidgate.aqa.util;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class OrderIdGenerator {

    public static String generate() {
        return "aqa-" + UUID.randomUUID();
    }
}
