package ru.yandex.practicum.dto.hub.scenario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ScenarioCondition {
    @NotBlank(message = "Идентификатор датчика, связанного с условием не может быть пустым")
    private String sensorId;

    @NotNull(message = "Типы условий, которые могут использоваться в сценариях, не могут быть нулевыми")
    private ScenarioConditionType type;

    @NotNull(message = "Операции, которые могут быть использованы в условиях, не могут быть нулевыми")
    private ScenarioConditionOperation operation;

    private Integer value;
}
