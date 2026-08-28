package solutis.lucas.afonso.helpdesk.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String NOTIFICATION_QUEUE = "notification-service.queue";

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public DirectExchange helpdeskExchange(@Value("${notification.rabbitmq.exchange}") String exchangeName) {
        return new DirectExchange(exchangeName, true, false);
    }

    @Bean
    public FanoutExchange notificationDeadLetterExchange(@Value("${notification.rabbitmq.dlx}") String dlxName) {
        return new FanoutExchange(dlxName, true, false);
    }

    @Bean
    public Queue notificationDeadLetterQueue(@Value("${notification.rabbitmq.dlq}") String dlqName) {
        return QueueBuilder.durable(dlqName).build();
    }

    @Bean
    public Binding notificationDeadLetterBinding(Queue notificationDeadLetterQueue,
            FanoutExchange notificationDeadLetterExchange) {
        return BindingBuilder.bind(notificationDeadLetterQueue).to(notificationDeadLetterExchange);
    }

    @Bean
    public Queue notificationQueue(@Value("${notification.rabbitmq.queue}") String queueName,
            @Value("${notification.rabbitmq.dlx}") String dlxName) {
        return QueueBuilder.durable(queueName)
                .withArgument("x-dead-letter-exchange", dlxName)
                .build();
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