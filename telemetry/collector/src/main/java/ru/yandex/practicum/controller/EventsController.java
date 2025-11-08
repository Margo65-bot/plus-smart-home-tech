package ru.yandex.practicum.controller;

import ru.yandex.practicum.dto.hub.HubEvent;
import ru.yandex.practicum.dto.sensor.SensorEvent;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.service.EventService;

@RestController
@RequestMapping("/events")
@Slf4j
@RequiredArgsConstructor
@Validated
public class EventsController {
    private final EventService eventService;

    @PostMapping("/sensors")
    @ResponseStatus(HttpStatus.OK)
    public void handleSensorEvent(@Valid @RequestBody SensorEvent event) {
        log.info("handleSensorEvent event.id={}, hub id={}, event.type={}", event.getId(), event.getHubId(), event.getType());
        eventService.handleSensorEvent(event);
    }

    @PostMapping("/hubs")
    @ResponseStatus(HttpStatus.OK)
    public void handleHubEvent(@Valid @RequestBody HubEvent event) {
        log.info("handleHubEvent hub id={}, event.type={}", event.getHubId(), event.getType());
        eventService.handleHubEvent(event);
    }
}
