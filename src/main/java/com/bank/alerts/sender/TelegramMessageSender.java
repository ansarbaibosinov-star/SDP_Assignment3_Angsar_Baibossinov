package com.bank.alerts.sender;

import com.bank.alerts.implementor.MessageSender;

public class TelegramMessageSender implements MessageSender {

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
            System.out.println("[Telegram] Channel unavailable.");
            return false;
        }

        System.out.println("\n[Telegram]");
        System.out.println("To: " + recipient);
        System.out.println("Title: " + title);
        System.out.println("Formatted message: **" + body + "**");
        System.out.println("Status: SENT");

        return true;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public String getChannelName() {
        return "Telegram";
    }
}