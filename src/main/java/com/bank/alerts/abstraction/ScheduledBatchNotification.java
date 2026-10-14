package com.bank.alerts.abstraction;

import com.bank.alerts.implementor.MessageSender;

import java.util.List;

public class ScheduledBatchNotification extends Notification {

    private final List<String> recipients;

    public ScheduledBatchNotification(
            MessageSender sender,
            List<String> recipients
    ) {
        super(sender);

        if (recipients == null) {
            throw new IllegalArgumentException(
                    "Recipients cannot be null"
            );
        }

        this.recipients = List.copyOf(recipients);
    }

    @Override
    public void send(String recipient, String message) {
        sendToAll(message);
    }

    public void sendToAll(String message) {
        int successful = 0;
        int failed = 0;

        System.out.println("\n=== SCHEDULED BATCH NOTIFICATION ===");
        System.out.println("Channel: " + sender.getChannelName());
        System.out.println("Recipients: " + recipients.size());

        if (!sender.isAvailable()) {
            System.out.println("Channel unavailable.");

            System.out.println("Total: " + recipients.size());
            System.out.println("Successful: 0");
            System.out.println("Failed: " + recipients.size());
            return;
        }

        for (String recipient : recipients) {
            boolean success = sender.sendMessage(
                    recipient,
                    "Banking Notification",
                    message
            );

            if (success) {
                successful++;
            } else {
                failed++;
            }
        }

        System.out.println("\n--- Batch Summary ---");
        System.out.println("Total: " + recipients.size());
        System.out.println("Successful: " + successful);
        System.out.println("Failed: " + failed);
    }

    public List<String> getRecipients() {
        return recipients;
    }
}