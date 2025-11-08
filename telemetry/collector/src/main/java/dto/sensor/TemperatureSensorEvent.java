package dto.sensor;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class TemperatureSensorEvent extends SensorEvent {
    @NotNull(message = "Температура в градусах Цельсия не может быть нулевой")
    @Min(value = -273, message = "Температура в градусах Цельсия не может быть ниже -273")
    @Max(value = 500, message = "Температура в градусах Цельсия не может быть выше 500")
    private Integer temperatureC;

    @NotNull(message = "Температура в градусах Фаренгейта не может быть нулевой")
    @Min(value = -459, message = "Температура в градусах Фаренгейта не может быть ниже -459")
    @Max(value = 932, message = "Температура в градусах Фаренгейта не может быть выше 932")
    private Integer temperatureF;

    @Override
    public SensorEventType getType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }
}
