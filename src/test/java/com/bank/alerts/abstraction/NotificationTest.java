package com.bank.alerts.abstraction;

import com.bank.alerts.implementor.MessageSender;
import com.bank.alerts.sender.PushNotificationSender;
import com.bank.alerts.util.MessageEncoder;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NotificationTest {

    private static class RecordingSender implements MessageSender {

        private final String channelName;
        private boolean available = true;
        private boolean sendResult = true;
        private int calls;
        private String lastRecipient;
        private String lastTitle;
        private String lastBody;

        RecordingSender(String channelName) {
            this.channelName = channelName;
        }

        @Override
        public boolean sendMessage(
                String recipient,
                String title,
                String body
        ) {
            calls++;
            lastRecipient = recipient;
            lastTitle = title;
            lastBody = body;

            return available && sendResult;
        }

        @Override
        public boolean isAvailable() {
            return available;
        }

        @Override
        public String getChannelName() {
            return channelName;
        }
    }

    @Test
    void urgentNotificationAddsUrgentPrefix() {
        RecordingSender primary = new RecordingSender("Test");
        RecordingSender fallback = new RecordingSender("Fallback");

        UrgentNotification notification =
                new UrgentNotification(primary, fallback);

        notification.send("customer1", "Suspicious transfer");

        assertEquals(
                "[URGENT] Suspicious transfer",
                primary.lastBody
        );
        assertEquals(1, primary.calls);
    }

    @Test
    void urgentNotificationUsesFallbackWhenPrimaryUnavailable() {
        RecordingSender primary = new RecordingSender("Primary");
        RecordingSender fallback = new RecordingSender("Fallback");

        primary.available = false;

        UrgentNotification notification =
                new UrgentNotification(primary, fallback);

        notification.send("customer1", "Suspicious login");

        assertEquals(0, primary.calls);
        assertEquals(1, fallback.calls);
        assertEquals(
                "[URGENT] Suspicious login",
                fallback.lastBody
        );
    }

    @Test
    void urgentNotificationFailsWhenBothChannelsUnavailable() {
        RecordingSender primary = new RecordingSender("Primary");
        RecordingSender fallback = new RecordingSender("Fallback");

        primary.available = false;
        fallback.available = false;

        UrgentNotification notification =
                new UrgentNotification(primary, fallback);

        String output = captureOutput(
                () -> notification.send("customer1", "Security alert")
        );

        assertEquals(0, primary.calls);
        assertEquals(0, fallback.calls);
        assertTrue(output.contains("Result: FAILURE"));
    }

    @Test
    void encryptedNotificationTransformsMessageBeforeSending() {
        RecordingSender sender = new RecordingSender("Test");

        EncryptedNotification notification =
                new EncryptedNotification(sender);

        String original = "Payment confirmed";

        notification.send("customer1", original);

        assertNotEquals(original, sender.lastBody);
        assertEquals(
                MessageEncoder.encode(original),
                sender.lastBody
        );
    }

    @Test
    void batchNotificationSendsToEveryRecipient() {
        RecordingSender sender = new RecordingSender("Test");

        ScheduledBatchNotification notification =
                new ScheduledBatchNotification(
                        sender,
                        List.of("customer1", "customer2", "customer3")
                );

        notification.sendToAll("Statement ready");

        assertEquals(3, sender.calls);
        assertEquals("customer3", sender.lastRecipient);
    }

    @Test
    void batchNotificationReportsAllSuccessfulDeliveries() {
        RecordingSender sender = new RecordingSender("Test");

        ScheduledBatchNotification notification =
                new ScheduledBatchNotification(
                        sender,
                        List.of("customer1", "customer2", "customer3")
                );

        String output = captureOutput(
                () -> notification.sendToAll("Statement ready")
        );

        assertTrue(output.contains("Total: 3"));
        assertTrue(output.contains("Successful: 3"));
        assertTrue(output.contains("Failed: 0"));
    }

    @Test
    void batchNotificationReportsFailedDeliveries() {
        RecordingSender sender = new RecordingSender("Test");
        sender.sendResult = false;

        ScheduledBatchNotification notification =
                new ScheduledBatchNotification(
                        sender,
                        List.of("customer1", "customer2")
                );

        String output = captureOutput(
                () -> notification.sendToAll("Statement ready")
        );

        assertTrue(output.contains("Total: 2"));
        assertTrue(output.contains("Successful: 0"));
        assertTrue(output.contains("Failed: 2"));
    }

    @Test
    void sameNotificationCanSwitchSenderAtRuntime() {
        RecordingSender telegram = new RecordingSender("Telegram");
        RecordingSender email = new RecordingSender("Email");
        RecordingSender fallback = new RecordingSender("Fallback");

        UrgentNotification notification =
                new UrgentNotification(telegram, fallback);

        notification.send("customer1", "Security update");

        notification.setSender(email);

        notification.send("customer1", "Security update");

        assertEquals(1, telegram.calls);
        assertEquals(1, email.calls);
        assertSame(email, notification.getSender());
    }

    @Test
    void marketingNotificationWorksWithPushSender() {
        PushNotificationSender push = new PushNotificationSender();

        MarketingNotification notification =
                new MarketingNotification(push);

        String output = captureOutput(
                () -> notification.send(
                        "customer1",
                        "Cashback offer"
                )
        );

        assertTrue(output.contains("[Push Notification]"));
        assertTrue(output.contains("[SPECIAL OFFER] Cashback offer"));
        assertTrue(output.contains("Result: SUCCESS"));
    }

    @Test
    void unavailableEncryptedNotificationDoesNotSend() {
        RecordingSender sender = new RecordingSender("Test");
        sender.available = false;

        EncryptedNotification notification =
                new EncryptedNotification(sender);

        String output = captureOutput(
                () -> notification.send("customer1", "Payment confirmed")
        );

        assertEquals(0, sender.calls);
        assertTrue(output.contains("Result: FAILURE"));
    }

    private String captureOutput(Runnable action) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try (PrintStream testOut = new PrintStream(
                output, true, StandardCharsets.UTF_8)) {

            System.setOut(testOut);
            action.run();

        } finally {
            System.setOut(originalOut);
        }

        return output.toString(StandardCharsets.UTF_8);
    }
}