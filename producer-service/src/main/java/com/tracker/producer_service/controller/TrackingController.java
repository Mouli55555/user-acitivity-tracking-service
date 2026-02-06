package com.tracker.producer_service.controller;

import com.tracker.producer_service.model.ActivityEvent;
import com.tracker.producer_service.service.EventPublisher;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/events")
public class TrackingController {

    private final EventPublisher publisher;
    private static final Logger log =
            LoggerFactory.getLogger(TrackingController.class);

    public TrackingController(EventPublisher publisher) {
        this.publisher = publisher;
    }

    /**
     * Receives user activity event and publishes to RabbitMQ
     */
    @PostMapping(
            path = "/track",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> trackUserActivity(
            @Valid @RequestBody ActivityEvent event
    ) {

        log.info("Received event: userId={}, eventType={}",
                event.getUserId(),
                event.getEventType());

        publisher.publish(event);

        return ResponseEntity
                .status(HttpStatus.ACCEPTED) // 202
                .body(
                        new ApiResponse(
                                "Event published successfully"
                        )
                );
    }
}
