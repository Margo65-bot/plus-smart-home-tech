package ru.yandex.practicum.dto.sensor;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class LightSensorEvent extends SensorEvent {
    @NotNull(message = "Качество связи не может быть нулевым")
    @Min(value = 0, message = "Качество связи не может быть ниже 0")
    @Max(value = 100, message = "Качество связи не может быть выше 100")
    private Integer linkQuality;

    @NotNull(message = "Уровень освещенности не может быть нулевым")
    @Min(value = 0, message = "Уровень освещенности не может быть ниже 0")
    @Max(value = 1000, message = "Уровень освещенности не может быть выше 1000")
    private Integer luminosity;

    @Override
    public SensorEventType getType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }
}
