package ru.yandex.practicum.collector.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import ru.yandex.practicum.collector.mapper.HubEventMapper;
import ru.yandex.practicum.collector.mapper.SensorEventMapper;
import ru.yandex.practicum.collector.model.hub.HubEvent;
import ru.yandex.practicum.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Service
@RequiredArgsConstructor
public class EventsServiceImpl implements EventsService {
    private final HubEventMapper hubEventMapper;
    private final SensorEventMapper sensorEventMapper;
    private final EventsKafkaSender kafkaSender;

    public void processHubEvent(HubEvent event) {
        HubEventAvro avroEvent = hubEventMapper.toAvro(event);
        kafkaSender.send(avroEvent);
    }


    public void processSensorEvent(SensorEvent event) {
        SensorEventAvro avroEvent = sensorEventMapper.toAvro(event);
        kafkaSender.send(avroEvent);
    }
}
