package ru.yandex.practicum.collector.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Slf4j
@Service
public class EventsKafkaSender {
    private final Producer<Void, SpecificRecordBase> kafkaProducer;
    private final String sensorEventsTopic;
    private final String hubEventsTopic;

    public EventsKafkaSender (Producer<Void, SpecificRecordBase> producer,
                             @Value("${kafka.topic.sensor-events}") String sensorEventsTopic,
                             @Value("${kafka.topic.hub-events}") String hubEventsTopic) {
        this.kafkaProducer = producer;
        this.sensorEventsTopic = sensorEventsTopic;
        this.hubEventsTopic = hubEventsTopic;
    }

    public void sendAsync(SensorEventAvro sensorEventAvro) {
        ProducerRecord<Void, SpecificRecordBase> record = new ProducerRecord<>(sensorEventsTopic, sensorEventAvro);

        kafkaProducer.send(record);
    }

    public void sendAsync(HubEventAvro hubEventAvro) {
        ProducerRecord<Void, SpecificRecordBase> record = new ProducerRecord<>(hubEventsTopic, hubEventAvro);

        kafkaProducer.send(record);
    }
}