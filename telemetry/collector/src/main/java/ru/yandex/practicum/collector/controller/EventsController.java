package ru.yandex.practicum.collector.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.collector.model.hub.HubEvent;
import ru.yandex.practicum.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.collector.service.EventsServiceImpl;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
public class EventsController {

    private final EventsServiceImpl eventsService;

    @PostMapping("/hubs")
    public ResponseEntity<Void> collectHubEvent(
            @Valid @RequestBody HubEvent event) {

        log.info("Got hub event: hubId={}, type={}",
                event.getHubId(), event.getType());

        eventsService.processHubEvent(event);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sensors")
    public ResponseEntity<Void> collectSensorEvent(
            @Valid @RequestBody SensorEvent event) {

        log.info("Got sensor event: id={}, type={}, hubId={}",
                event.getId(), event.getType(), event.getHubId());

        eventsService.processSensorEvent(event);
        return ResponseEntity.ok().build();
    }
}