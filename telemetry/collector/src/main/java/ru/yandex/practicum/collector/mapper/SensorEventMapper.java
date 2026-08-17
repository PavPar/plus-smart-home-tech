package ru.yandex.practicum.collector.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.model.sensor.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

@Component
public class SensorEventMapper {

    public SensorEventAvro toAvro(SensorEvent event) {
        if (event == null) {
            return null;
        }

        SensorEventAvro.Builder builder = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp());

        switch (event) {
            case LightSensorEvent light -> {
                LightSensorAvro payload = LightSensorAvro.newBuilder()
                        .setLinkQuality(light.getLinkQuality())
                        .setLuminosity(light.getLuminosity())
                        .build();
                builder.setPayload(payload);
            }
            case MotionSensorEvent motion -> {
                MotionSensorAvro payload = MotionSensorAvro.newBuilder()
                        .setLinkQuality(motion.getLinkQuality())
                        .setMotion(motion.isMotion())
                        .setVoltage(motion.getVoltage())
                        .build();
                builder.setPayload(payload);
            }
            case TemperatureSensorEvent temp -> {
                TemperatureSensorAvro payload = TemperatureSensorAvro.newBuilder()
                        .setId(temp.getId())
                        .setHubId(temp.getHubId())
                        .setTimestamp(temp.getTimestamp())
                        .setTemperatureC(temp.getTemperatureC())
                        .setTemperatureF(temp.getTemperatureF())
                        .build();
                builder.setPayload(payload);

            }
            case ClimateSensorEvent climate -> {
                ClimateSensorAvro payload = ClimateSensorAvro.newBuilder()
                        .setTemperatureC(climate.getTemperatureC())
                        .setHumidity(climate.getHumidity())
                        .setCo2Level(climate.getCo2Level())
                        .build();
                builder.setPayload(payload);

            }
            case SwitchSensorEvent switchEvent -> {
                SwitchSensorAvro payload = SwitchSensorAvro.newBuilder()
                        .setState(switchEvent.isState())
                        .build();
                builder.setPayload(payload);

            }
            default ->
                throw new IllegalArgumentException("Незветный тип" + event.getClass());
        }

        return builder.build();
    }
}