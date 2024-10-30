package quickbit.notification.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.MessageChannel;
import quickbit.notification.observer.EmailEventObserver;
import quickbit.notification.observer.EventObserver;
import quickbit.notification.service.RabbitMQSender;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableIntegration
public class NotificationIntegrationConfig {

    private final NotificationGateway notificationGateway;

    @Autowired
    public NotificationIntegrationConfig(NotificationGateway notificationGateway) {
        this.notificationGateway = notificationGateway;
    }

    @Bean("basicObservers")
    public Set<EventObserver> basicObservers() {
        Set<EventObserver> observers = new HashSet<>();

        observers.add(new EmailEventObserver(notificationGateway));

        return observers;
    }

    @Bean
    public MessageChannel notificationChannel() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow notificationFlow(RabbitMQSender rabbitMQSender) {
        return IntegrationFlows
            .from("notificationChannel")
            .handle((payload, headers) -> {
                    rabbitMQSender.sendToQueue((String) payload);
                    return null;
                })
            .get();
    }
}
