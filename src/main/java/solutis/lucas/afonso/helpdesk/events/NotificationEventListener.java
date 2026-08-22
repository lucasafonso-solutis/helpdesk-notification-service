package solutis.lucas.afonso.helpdesk.events;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import solutis.lucas.afonso.helpdesk.controllers.NotificationController;
import solutis.lucas.afonso.helpdesk.config.RabbitMQConfig;
import solutis.lucas.afonso.helpdesk.model.Notification;

import java.time.LocalDateTime;

@Component
public class NotificationEventListener {
	private final ObjectMapper objectMapper;
	private final NotificationController notificationController;

	public NotificationEventListener(ObjectMapper objectMapper, NotificationController notificationController) {
		this.objectMapper = objectMapper;
		this.notificationController = notificationController;
	}

	@RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE)
	public void receive(String payload, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey)
			throws Exception {
		JsonNode event = objectMapper.readTree(payload);
		Long ticketId = longValue(event, "ticketId");
		Long customerId = longValue(event, "customerId");
		Long technicianId = longValue(event, "technicianId");
		String status = textValue(event, "status");
		String message = createMessage(routingKey, event, ticketId);

		notificationController.save(new Notification(routingKey, ticketId, customerId, technicianId,
				status, message, LocalDateTime.now()));
	}

	private String createMessage(String routingKey, JsonNode event, Long ticketId) {
		return switch (routingKey) {
			case "ticket.created" -> "Ticket " + ticketId + " created";
			case "ticket.assigned" -> "Ticket " + ticketId + " assigned to technician "
					+ longValue(event, "technicianId");
			case "ticket.status.changed" -> "Ticket " + ticketId + " changed to status "
					+ textValue(event, "status");
			default -> "Ticket " + ticketId + " event received";
		};
	}

	private Long longValue(JsonNode event, String field) {
		JsonNode value = event.get(field);
		return value == null || value.isNull() ? null : value.longValue();
	}

	private String textValue(JsonNode event, String field) {
		JsonNode value = event.get(field);
		return value == null || value.isNull() ? null : value.asText();
	}
}