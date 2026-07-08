package ru.yandex.practicum.collector.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.model.hub.*;
import ru.yandex.practicum.collector.model.utils.DeviceAction;
import ru.yandex.practicum.collector.model.utils.ScenarioCondition;
import ru.yandex.practicum.kafka.telemetry.event.*;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class HubEventMapper {

    public HubEventAvro toAvro(HubEvent event) {
        if (event == null) {
            return null;
        }

        HubEventAvro.Builder builder = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp());

        switch (event) {
            case DeviceAddedEvent added -> {
                DeviceAddedEventAvro payload = DeviceAddedEventAvro.newBuilder()
                        .setId(added.getId())
                        .setType(DeviceTypeAvro.valueOf(added.getDeviceType().toString()))
                        .build();
                builder.setPayload(payload);

            }
            case DeviceRemovedEvent removed -> {
                DeviceRemovedEventAvro payload = DeviceRemovedEventAvro.newBuilder()
                        .setId(removed.getId())
                        .build();
                builder.setPayload(payload);

            }
            case ScenarioAddedEvent scenario -> {

                List<ScenarioConditionAvro> conditions = scenario.getConditions().stream()
                        .map(this::toScenarioConditionAvro)
                        .collect(Collectors.toList());

                List<DeviceActionAvro> actions = scenario.getActions().stream()
                        .map(this::toDeviceActionAvro)
                        .collect(Collectors.toList());

                ScenarioAddedEventAvro payload = ScenarioAddedEventAvro.newBuilder()
                        .setName(scenario.getName())
                        .setConditions(conditions)
                        .setActions(actions)
                        .build();
                builder.setPayload(payload);

            }
            case ScenarioRemovedEvent removed -> {
                ScenarioRemovedEventAvro payload = ScenarioRemovedEventAvro.newBuilder()
                        .setName(removed.getName())
                        .build();
                builder.setPayload(payload);

            }
            default -> {
                throw new IllegalArgumentException("Незветный тип" + event.getClass());
            }
        }

        return builder.build();
    }

    private ScenarioConditionAvro toScenarioConditionAvro(ScenarioCondition condition) {
        ScenarioConditionAvro.Builder builder = ScenarioConditionAvro.newBuilder()
                .setSensorId(condition.getSensorId())
                .setType(ConditionTypeAvro.valueOf(condition.getType()))
                .setOperation(ConditionOperationAvro.valueOf(condition.getOperation()));

        Object value = condition.getValue();
        if (value instanceof Integer) {
            builder.setValue((Integer) value);
        } else if (value instanceof Boolean) {
            builder.setValue((Boolean) value);
        } else {
            builder.setValue(null);
        }

        return builder.build();
    }

    private DeviceActionAvro toDeviceActionAvro(DeviceAction action) {
        DeviceActionAvro.Builder builder = DeviceActionAvro.newBuilder()
                .setSensorId(action.getSensorId())
                .setType(ActionTypeAvro.valueOf(action.getType()));

        if (action.getValue() != null) {
            builder.setValue(action.getValue());
        } else {
            builder.setValue(null);
        }

        return builder.build();
    }
}