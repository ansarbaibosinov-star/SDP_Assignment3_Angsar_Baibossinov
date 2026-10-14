package com.bank.alerts.abstraction;

import com.bank.alerts.implementor.MessageSender;
import com.bank.alerts.util.MessageEncoder;

public class EncryptedNotification extends Notification {

    public EncryptedNotification(MessageSender sender) {
        super(sender);
    }

    @Override
    public void send(String recipient, String message) {
        String encodedMessage = MessageEncoder.encode(message);

        System.out.println("\n=== ENCRYPTED BANK ALERT ===");
        System.out.println("Channel: " + sender.getChannelName());

        if (!sender.isAvailable()) {
            System.out.println("Channel unavailable.");
            System.out.println("Result: FAILURE");
            return;
        }

        boolean success = sender.sendMessage(
                recipient,
                "Secure Banking Notification",
                encodedMessage
        );

        System.out.println(
                success ? "Result: SUCCESS" : "Result: FAILURE"
        );
    }
}