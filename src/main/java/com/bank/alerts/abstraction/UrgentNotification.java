package com.bank.alerts.abstraction;

import com.bank.alerts.implementor.MessageSender;

public class UrgentNotification extends Notification {

    private final MessageSender fallbackSender;

    public UrgentNotification(
            MessageSender sender,
            MessageSender fallbackSender
    ) {
        super(sender);

        if (fallbackSender == null) {
            throw new IllegalArgumentException(
                    "Fallback sender cannot be null"
            );
        }

        this.fallbackSender = fallbackSender;
    }

    @Override
    public void send(String recipient, String message) {
        String urgentMessage = "[URGENT] " + message;
        String title = "Bank Security Alert";

        System.out.println("\n=== URGENT BANK ALERT ===");
        System.out.println("Primary channel: "
                + sender.getChannelName());

        if (sender.isAvailable()) {
            boolean success = sender.sendMessage(
                    recipient,
                    title,
                    urgentMessage
            );

            if (success) {
                System.out.println("Result: SUCCESS");
                return;
            }

            System.out.println(
                    "Primary delivery failed. Trying fallback..."
            );
        } else {
            System.out.println(
                    "Primary channel unavailable. Trying fallback..."
            );
        }

        System.out.println("Fallback channel: "
                + fallbackSender.getChannelName());

        if (!fallbackSender.isAvailable()) {
            System.out.println(
                    "Fallback channel is also unavailable."
            );
            System.out.println("Result: FAILURE");
            return;
        }

        boolean fallbackSuccess = fallbackSender.sendMessage(
                recipient,
                title,
                urgentMessage
        );

        System.out.println(
                fallbackSuccess
                        ? "Result: SUCCESS (fallback)"
                        : "Result: FAILURE"
        );
    }

    public MessageSender getFallbackSender() {
        return fallbackSender;
    }
}