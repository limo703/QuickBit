package quickbit.notification.configuration;

import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(defaultRequestChannel = "notificationChannel")
public interface NotificationGateway {
    void send(String message);
}
