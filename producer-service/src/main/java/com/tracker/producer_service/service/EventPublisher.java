package com.tracker.producer_service.service;

import com.tracker.producer_service.config.RabbitMQConfig;
import com.tracker.producer_service.model.ActivityEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final Logger log = LoggerFactory.getLogger(EventPublisher.class);

    public EventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(ActivityEvent event) {
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME, event);
            log.info("Published event for userId={} eventType={}", event.getUserId(), event.getEventType());
        } catch (AmqpException ex) {
            log.error("Failed to publish event: {}", ex.getMessage(), ex);
            throw ex;
        }
    }
}
