package com.bank.alerts.abstraction;

import com.bank.alerts.implementor.MessageSender;

public abstract class Notification {

    protected MessageSender sender;

    protected Notification(MessageSender sender) {
        if (sender == null) {
            throw new IllegalArgumentException(
                    "Message sender cannot be null"
            );
        }

        this.sender = sender;
    }

    public void setSender(MessageSender sender) {
        if (sender == null) {
            throw new IllegalArgumentException(
                    "Message sender cannot be null"
            );
        }

        this.sender = sender;
    }

    public MessageSender getSender() {
        return sender;
    }

    public abstract void send(
            String recipient,
            String message
    );
}