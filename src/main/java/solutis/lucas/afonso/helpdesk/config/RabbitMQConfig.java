package solutis.lucas.afonso.helpdesk.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String NOTIFICATION_QUEUE = "notification-service.queue";

    @Bean
    public DirectExchange helpdeskExchange(@Value("${notification.rabbitmq.exchange}") String exchangeName) {
        return new DirectExchange(exchangeName, true, false);
    }

    @Bean
    public Queue notificationQueue(@Value("${notification.rabbitmq.queue}") String queueName) {
        return QueueBuilder.durable(queueName).build();
    }

    @Bean
    public Binding ticketCreatedBinding(Queue notificationQueue, DirectExchange helpdeskExchange,
            @Value("${notification.rabbitmq.ticket-created-routing-key}") String routingKey) {
        return BindingBuilder.bind(notificationQueue).to(helpdeskExchange).with(routingKey);
    }

    @Bean
    public Binding ticketAssignedBinding(Queue notificationQueue, DirectExchange helpdeskExchange,
            @Value("${notification.rabbitmq.ticket-assigned-routing-key}") String routingKey) {
        return BindingBuilder.bind(notificationQueue).to(helpdeskExchange).with(routingKey);
    }

    @Bean
    public Binding ticketStatusChangedBinding(Queue notificationQueue, DirectExchange helpdeskExchange,
            @Value("${notification.rabbitmq.ticket-status-changed-routing-key}") String routingKey) {
        return BindingBuilder.bind(notificationQueue).to(helpdeskExchange).with(routingKey);
    }
}