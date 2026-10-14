package com.bank.alerts;

import com.bank.alerts.abstraction.EncryptedNotification;
import com.bank.alerts.abstraction.MarketingNotification;
import com.bank.alerts.abstraction.ScheduledBatchNotification;
import com.bank.alerts.abstraction.UrgentNotification;
import com.bank.alerts.sender.EmailMessageSender;
import com.bank.alerts.sender.PushNotificationSender;
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

        PushNotificationSender push =
                new PushNotificationSender();

        // 1. UrgentNotification + Telegram
        System.out.println("\n--- DEMO 1: URGENT + TELEGRAM ---");

        UrgentNotification urgent =
                new UrgentNotification(telegram, sms);

        urgent.send(
                "customer123",
                "Suspicious transaction detected: 500000 KZT"
        );

        // 2. EncryptedNotification + Email
        System.out.println("\n--- DEMO 2: ENCRYPTED + EMAIL ---");

        EncryptedNotification encrypted =
                new EncryptedNotification(email);

        encrypted.send(
                "customer456",
                "Payment confirmed: 25000 KZT"
        );

        // 3. ScheduledBatchNotification + SMS
        System.out.println("\n--- DEMO 3: BATCH + SMS ---");

        ScheduledBatchNotification batch =
                new ScheduledBatchNotification(
                        sms,
                        List.of(
                                "customer101",
                                "customer102",
                                "customer103"
                        )
                );

        batch.sendToAll(
                "Your monthly bank statement is ready."
        );

        // 4. Runtime switching: Telegram -> Email
        System.out.println("\n--- DEMO 4: RUNTIME SWITCHING ---");

        urgent.setSender(telegram);

        urgent.send(
                "customer123",
                "Your account security status has been updated."
        );

        urgent.setSender(email);

        urgent.send(
                "customer123",
                "Your account security status has been updated."
        );

        // 5. Telegram unavailable -> fallback to SMS
        System.out.println("\n--- DEMO 5: FALLBACK TO SMS ---");

        telegram.setAvailable(false);
        urgent.setSender(telegram);

        urgent.send(
                "customer123",
                "Suspicious login detected from a new device."
        );

        // 6. MarketingNotification + PushNotificationSender
        System.out.println("\n--- DEMO 6: MARKETING + PUSH ---");

        MarketingNotification marketing =
                new MarketingNotification(push);

        marketing.send(
                "customer789",
                "Get 5% cashback on eligible purchases this month!"
        );
    }
}