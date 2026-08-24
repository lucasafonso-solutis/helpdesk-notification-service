package solutis.lucas.afonso.helpdesk.events;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import solutis.lucas.afonso.helpdesk.dtos.NotificationDTO;
import solutis.lucas.afonso.helpdesk.model.EventType;
import solutis.lucas.afonso.helpdesk.services.NotificationService;

@Component
public class NotificationEventListener {
	private final ObjectMapper objectMapper;
	private final NotificationService notificationService;

	public NotificationEventListener(ObjectMapper objectMapper, NotificationService notificationService) {
		this.objectMapper = objectMapper;
		this.notificationService = notificationService;
	}

	@RabbitListener(queues = "${notification.rabbitmq.queue}")
	public void receive(String payload, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) throws Exception {
		JsonNode event = objectMapper.readTree(payload);
		Long ticketId = longValue(event, "ticketId");
		Long customerId = longValue(event, "customerId");
		Long technicianId = longValue(event, "technicianId");
		String status = textValue(event, "status");
		String message = createMessage(routingKey, event, ticketId);
		EventType eventType = eventTypeFor(routingKey);

		notificationService.create(new NotificationDTO(null, eventType, ticketId, customerId, technicianId, status, message, null));
	}

	private EventType eventTypeFor(String routingKey) {
		return switch (routingKey) {
			case "ticket.created" -> EventType.TICKET_CREATED;
			case "ticket.assigned" -> EventType.TICKET_ASSIGNED;
			case "ticket.status.changed" -> EventType.TICKET_STATUS_CHANGED;
			default -> throw new IllegalArgumentException("Unsupported notification event: " + routingKey);
		};
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