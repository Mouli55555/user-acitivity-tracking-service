package com.tracker.consumer_service.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracker.consumer_service.config.RabbitMQConfig;
import com.tracker.consumer_service.entity.UserActivity;
import com.tracker.consumer_service.repository.UserActivityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.Map;

@Service
public class ActivityListener {

    private final UserActivityRepository repository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Logger log = LoggerFactory.getLogger(ActivityListener.class);

    public ActivityListener(UserActivityRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveMessage(Map<String, Object> event) {
        try {
            UserActivity activity = new UserActivity();

            // userId can be Number or String
            Object uidObj = event.get("userId");
            if (uidObj instanceof Number) {
                activity.setUserId(((Number) uidObj).intValue());
            } else if (uidObj instanceof String) {
                try {
                    activity.setUserId(Integer.parseInt((String) uidObj));
                } catch (NumberFormatException e) {
                    log.warn("Invalid userId: {}", uidObj);
                    activity.setUserId(0);
                }
            } else {
                log.warn("userId missing or unexpected type: {}", uidObj);
                activity.setUserId(0);
            }

            activity.setEventType(String.valueOf(event.getOrDefault("eventType", "unknown")));

            // Parse timestamp — accept ISO-8601 strings like "2026-02-06T12:34:56" or epoch millis
            Object tsObj = event.get("timestamp");
            activity.setTimestamp(parseTimestamp(tsObj));

            // Convert metadata object to JSON string (safe)
            Object metadataObj = event.get("metadata");
            if (metadataObj != null) {
                try {
                    String metadataJson = objectMapper.writeValueAsString(metadataObj);
                    activity.setMetadata(metadataJson);
                } catch (JsonProcessingException e) {
                    activity.setMetadata(String.valueOf(metadataObj));
                }
            } else {
                activity.setMetadata(null);
            }

            repository.save(activity);
            log.info("✅ Saved event into MySQL: userId={}, eventType={}", activity.getUserId(), activity.getEventType());

        } catch (Exception ex) {
            log.error("Error processing message: {}", ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }
    }

    private LocalDateTime parseTimestamp(Object tsObj) {
        if (tsObj == null) {
            return LocalDateTime.now();
        }
        try {
            if (tsObj instanceof Number) {
                long millis = ((Number) tsObj).longValue();
                return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
            } else {
                String tsStr = tsObj.toString();
                // Try OffsetDateTime (ISO)
                try {
                    OffsetDateTime odt = OffsetDateTime.parse(tsStr);
                    return odt.toLocalDateTime();
                } catch (DateTimeParseException ignored) {}

                try {
                    return LocalDateTime.parse(tsStr);
                } catch (DateTimeParseException ignored) {}

                // epoch seconds or millis without quotes
                try {
                    long epoch = Long.parseLong(tsStr);
                    // choose heuristic: if length > 10 treat as millis
                    if (tsStr.length() > 10) {
                        return LocalDateTime.ofInstant(Instant.ofEpochMilli(epoch), ZoneId.systemDefault());
                    } else {
                        return LocalDateTime.ofInstant(Instant.ofEpochSecond(epoch), ZoneId.systemDefault());
                    }
                } catch (NumberFormatException ignored) {}
            }
        } catch (Exception e) {
            log.warn("Unable to parse timestamp '{}', using now. Error: {}", tsObj, e.getMessage());
        }
        return LocalDateTime.now();
    }
}
