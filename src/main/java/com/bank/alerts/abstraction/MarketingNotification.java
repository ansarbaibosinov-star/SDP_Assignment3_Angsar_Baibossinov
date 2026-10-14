package com.bank.alerts.abstraction;

import com.bank.alerts.implementor.MessageSender;

public class MarketingNotification extends Notification {

    public MarketingNotification(MessageSender sender) {
        super(sender);
    }

    @Override
    public void send(String recipient, String message) {
        String marketingMessage = "[SPECIAL OFFER] " + message;

        System.out.println("\n=== BANK MARKETING NOTIFICATION ===");
        System.out.println("Channel: " + sender.getChannelName());

        if (!sender.isAvailable()) {
            System.out.println("Channel unavailable.");
            System.out.println("Result: FAILURE");
            return;
        }

        boolean success = sender.sendMessage(
                recipient,
                "Special Offer from Your Bank",
                marketingMessage
        );

        System.out.println(
                success ? "Result: SUCCESS" : "Result: FAILURE"
        );
    }
}