package com.bank.alerts.implementor;

public interface MessageSender {

    boolean sendMessage(
            String recipient,
            String title,
            String body
    );

    boolean isAvailable();

    String getChannelName();
}