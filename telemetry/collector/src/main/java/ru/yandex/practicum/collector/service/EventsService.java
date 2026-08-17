package ru.yandex.practicum.collector.service;

import ru.yandex.practicum.collector.model.hub.HubEvent;
import ru.yandex.practicum.collector.model.sensor.SensorEvent;

public interface EventsService {
    void processSensorEvent(SensorEvent sensorEvent);

    void processHubEvent(HubEvent hubEvent);
}