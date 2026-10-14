package com.bank.alerts;

import com.bank.alerts.abstraction.EncryptedNotification;
import com.bank.alerts.abstraction.ScheduledBatchNotification;
import com.bank.alerts.abstraction.UrgentNotification;
import com.bank.alerts.sender.EmailMessageSender;
import com.bank.alerts.sender.SmsMessageSender;
import com.bank.alerts.sender.TelegramMessageSender;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        TelegramMessageSender telegram =
                new TelegramMessageSender();

        EmailMessageSender email =
                new EmailMessageSender();

        SmsMessageSender sms =
                new SmsMessageSender();

        // Scenario 1: Urgent notification via Telegram.
        UrgentNotification urgent =
                new UrgentNotification(telegram, sms);

        urgent.send(
                "customer123",
                "Suspicious transaction detected: 500000 KZT"
        );

        // Scenario 2: Encrypted notification via Email.
        EncryptedNotification encrypted =
                new EncryptedNotification(email);

        encrypted.send(
                "customer456",
                "Payment confirmed: 25000 KZT"
        );

        // Scenario 3: Batch notification via SMS.
        ScheduledBatchNotification batch =
                new ScheduledBatchNotification(
                        sms,
                        List.of(
                                "User1",
                                "User2",
                                "User3"
                        )
                );

        batch.sendToAll(
                "Your monthly bank statement is ready."
        );

        // Scenario 4: Telegram unavailable, fallback to SMS.
        telegram.setAvailable(false);

        urgent.send(
                "customer123",
                "Suspicious login detected."
        );
    }
}