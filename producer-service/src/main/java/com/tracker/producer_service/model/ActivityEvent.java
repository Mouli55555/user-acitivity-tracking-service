package com.tracker.producer_service.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public class ActivityEvent {

    @NotNull
    private Integer userId;

    @NotBlank
    private String eventType;

    /**
     * Accept ISO timestamp or epoch millis as string/number.
     */
    private String timestamp;

    private Map<String, Object> metadata;

    // getters and setters
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
}
