package com.bank.alerts.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class MessageEncoder {

    private MessageEncoder() {
        // Utility class: objects are not needed.
    }

    public static String encode(String message) {
        if (message == null) {
            throw new IllegalArgumentException(
                    "Message cannot be null"
            );
        }

        return Base64.getEncoder().encodeToString(
                message.getBytes(StandardCharsets.UTF_8)
        );
    }
}