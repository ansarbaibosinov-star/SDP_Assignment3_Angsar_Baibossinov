package com.bank.alerts;

import com.bank.alerts.abstraction.UrgentNotification;
import com.bank.alerts.sender.SmsMessageSender;
import com.bank.alerts.sender.TelegramMessageSender;

public class Main {

    public static void main(String[] args) {

        TelegramMessageSender telegram =
                new TelegramMessageSender();

        SmsMessageSender sms =
                new SmsMessageSender();

        UrgentNotification notification =
                new UrgentNotification(telegram, sms);

        // Scenario 1: Telegram is available.
        notification.send(
                "customer123",
                "Suspicious transaction detected: 500000 KZT"
        );

        // Scenario 2: Telegram becomes unavailable.
        telegram.setAvailable(false);

        notification.send(
                "customer123",
                "Suspicious login detected from a new device"
        );
    }
}