# Banking Alert System — Bridge Pattern

## 1. Project Overview

The Banking Alert System demonstrates the **Bridge Design Pattern** using Java 17 and Maven.

The system sends simulated banking notifications through different delivery channels. It separates notification types from message delivery providers, allowing both sides to change independently.

No real emails, SMS messages, push notifications, or Kafka events are sent.

## 2. Technologies

- Java 17
- Maven
- JUnit 5
- Bridge Design Pattern
- Git and GitHub

## 3. Project Structure

```text
src/
├── main/java/com/bank/alerts/
│   ├── abstraction/
│   │   ├── Notification.java
│   │   ├── UrgentNotification.java
│   │   ├── EncryptedNotification.java
│   │   ├── ScheduledBatchNotification.java
│   │   └── MarketingNotification.java
│   ├── implementor/
│   │   └── MessageSender.java
│   ├── sender/
│   │   ├── TelegramMessageSender.java
│   │   ├── EmailMessageSender.java
│   │   ├── SmsMessageSender.java
│   │   ├── KafkaEventSender.java
│   │   └── PushNotificationSender.java
│   ├── util/
│   │   └── MessageEncoder.java
│   └── Main.java
└── test/java/com/bank/alerts/
    ├── sender/
    │   └── MessageSenderTest.java
    └── abstraction/
        └── NotificationTest.java
```

## 4. Main Components

### Abstraction

`Notification` is the abstract base class. It contains a reference to `MessageSender` and allows the delivery provider to be changed at runtime.

### Refined Abstractions

- `UrgentNotification` adds an urgent prefix and supports a fallback provider.
- `EncryptedNotification` encodes the message before sending it.
- `ScheduledBatchNotification` sends a notification to multiple recipients and reports delivery statistics.
- `MarketingNotification` sends promotional banking messages.

### Implementor

`MessageSender` defines the common delivery interface:

- `sendMessage()`
- `isAvailable()`
- `getChannelName()`

### Concrete Implementors

- `TelegramMessageSender`
- `EmailMessageSender`
- `SmsMessageSender`
- `KafkaEventSender`
- `PushNotificationSender`

Each provider implements the same interface but handles message delivery differently.

## 5. Demonstration Scenarios

The `Main` class demonstrates:

1. Sending an urgent notification through Telegram.
2. Encoding a notification before sending it through Email.
3. Sending a batch notification to multiple recipients through SMS.
4. Switching a notification object's provider at runtime.
5. Using SMS as a fallback when Telegram is unavailable.
6. Sending a marketing notification through Push Notifications.

## 6. How to Run

Requirements: Java 17 and Maven.

Run the unit tests:

```bash
mvn clean test
```

Run the demonstration:

```bash
mvn compile
```

Then run `com.bank.alerts.Main` from your IDE, or execute it using your preferred Maven/Java run configuration.

## 7. Unit Tests

JUnit 5 tests cover provider behavior, availability checks, SMS length validation, fallback delivery, message encoding, batch statistics, runtime provider switching, and marketing notifications.

## 8. Bridge Pattern — Questions and Answers

### 1. What are the two dimensions of variation?

The first dimension is the **notification type**, such as urgent, encrypted, batch, or marketing notifications.

The second dimension is the **delivery provider**, such as Telegram, Email, SMS, Kafka, or Push Notifications.

### 2. Where is the Bridge Pattern implemented?

The bridge is the `MessageSender` reference inside the abstract `Notification` class. A notification delegates delivery to the provider instead of implementing a delivery channel itself.

### 3. Why not use inheritance alone?

If every notification type had a separate subclass for every provider, the number of classes would grow quickly. For example, four notification types and five providers could require up to twenty combinations. Bridge keeps these dimensions separate.

### 4. How can a new provider be added?

Create a new class that implements `MessageSender`. Existing notification classes do not need to be changed, provided the new provider follows the interface contract.

### 5. How can a new notification type be added?

Create a subclass of `Notification` and implement its `send()` behavior. Existing delivery providers can be reused.

### 6. What is the difference between Bridge and Adapter?

Bridge separates two dimensions of a design so they can evolve independently. Adapter converts an existing interface into another interface expected by a client. Bridge focuses on independent variation; Adapter focuses on compatibility.

## 9. Important Note

`MessageEncoder` uses Base64 encoding for demonstration purposes. **Base64 is not encryption and does not provide real security.** A production banking system would require authenticated encryption, secure key management, access controls, and other security measures.
