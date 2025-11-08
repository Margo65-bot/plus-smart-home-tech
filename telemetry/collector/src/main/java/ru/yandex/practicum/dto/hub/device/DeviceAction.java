package ru.yandex.practicum.dto.hub.device;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class DeviceAction {
    @NotBlank(message = "Идентификатор датчика, связанного с действием, не может быть пустым")
    private String sensorId;

    @NotNull(message = "Перечисление возможных типов действий при срабатывании условия активации сценария не может быть нулевым")
    private DeviceActionType type;

    private Integer value;
}
