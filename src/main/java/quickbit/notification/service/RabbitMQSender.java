package quickbit.notification.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSender {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMQSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendToQueue(String message) {
        rabbitTemplate.convertAndSend("emailExchange", "emailRoutingKey", message);
        System.out.println("Сообщение отправлено в RabbitMQ: " + message);
    }
}
