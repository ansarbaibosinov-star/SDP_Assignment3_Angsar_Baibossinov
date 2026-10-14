package com.bank.alerts.sender;

import com.bank.alerts.implementor.MessageSender;

public class KafkaEventSender implements MessageSender {

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
            System.out.println("[Kafka] Broker unavailable.");
            return false;
        }

        System.out.println("\n[Kafka Event]");
        System.out.println("Topic: banking-alerts");
        System.out.println("Event recipient: " + recipient);
        System.out.println("Event title: " + title);
        System.out.println("Event payload: " + body);
        System.out.println("Status: PUBLISHED (simulated)");

        return true;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public String getChannelName() {
        return "Kafka";
    }
}