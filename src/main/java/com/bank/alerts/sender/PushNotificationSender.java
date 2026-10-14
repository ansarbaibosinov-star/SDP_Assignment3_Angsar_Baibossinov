package com.bank.alerts.sender;

import com.bank.alerts.implementor.MessageSender;

public class PushNotificationSender implements MessageSender {

    private boolean available = true;

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public boolean sendMessage(
            String recipient,
            String title,
            String body
    ) {
        if (!isAvailable()) {
            System.out.println("[Push] Service unavailable.");
            return false;
        }

        System.out.println("\n[Push Notification]");
        System.out.println("Device/User: " + recipient);
        System.out.println("Title: " + title);
        System.out.println("Push content: " + body);
        System.out.println("Status: DELIVERED (simulated)");

        return true;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public String getChannelName() {
        return "Push Notification";
    }
}