package com.bank.alerts.sender;

import com.bank.alerts.implementor.MessageSender;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageSenderTest {

    @Test
    void telegramSendsMessageSuccessfully() {
        TelegramMessageSender sender =
                new TelegramMessageSender();

        boolean result = sender.sendMessage(
                "customer1",
                "Security Alert",
                "Login detected"
        );

        assertTrue(result);
        assertTrue(sender.isAvailable());
        assertEquals("Telegram", sender.getChannelName());
    }

    @Test
    void unavailableTelegramRejectsMessage() {
        TelegramMessageSender sender =
                new TelegramMessageSender();

        sender.setAvailable(false);

        boolean result = sender.sendMessage(
                "customer1",
                "Security Alert",
                "Login detected"
        );

        assertFalse(result);
        assertFalse(sender.isAvailable());
    }

    @Test
    void smsAcceptsMessageWithin160Characters() {
        SmsMessageSender sender = new SmsMessageSender();

        boolean result = sender.sendMessage(
                "customer1",
                "",
                "A".repeat(160)
        );

        assertTrue(result);
    }

    @Test
    void smsRejectsMessageLongerThan160Characters() {
        SmsMessageSender sender = new SmsMessageSender();

        boolean result = sender.sendMessage(
                "customer1",
                "",
                "A".repeat(161)
        );

        assertFalse(result);
    }

    @Test
    void emailRejectsMessageWhenUnavailable() {
        EmailMessageSender sender =
                new EmailMessageSender();

        sender.setAvailable(false);

        boolean result = sender.sendMessage(
                "customer1",
                "Payment",
                "Payment completed"
        );

        assertFalse(result);
    }

    @Test
    void kafkaPublishesEventSuccessfully() {
        KafkaEventSender sender = new KafkaEventSender();

        boolean result = sender.sendMessage(
                "customer1",
                "Transfer",
                "Transfer completed"
        );

        assertTrue(result);
        assertEquals("Kafka", sender.getChannelName());
    }

    @Test
    void pushRejectsMessageWhenUnavailable() {
        PushNotificationSender sender =
                new PushNotificationSender();

        sender.setAvailable(false);

        boolean result = sender.sendMessage(
                "customer1",
                "Offer",
                "Cashback"
        );

        assertFalse(result);
    }
}