package com.solidgate.aqa.browser;

import java.util.Arrays;

public enum BrowserType {
    CHROME,
    FIREFOX,
    EDGE,
    SAFARI;

    public static BrowserType fromSystemProperty() {
        String name = System.getProperty("browser", "chrome");
        return fromString(name);
    }

    public static BrowserType fromString(String value) {
        return Arrays.stream(values())
                .filter(b -> b.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unsupported browser: '" + value + "'. Supported: " + Arrays.toString(values())));
    }

    public String selenideName() {
        return name().toLowerCase();
    }
}
