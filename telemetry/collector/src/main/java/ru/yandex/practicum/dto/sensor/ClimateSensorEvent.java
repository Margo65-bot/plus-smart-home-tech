package ru.yandex.practicum.dto.sensor;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class ClimateSensorEvent extends SensorEvent {
    @NotNull(message = "Уровень температуры по шкале Цельсия не может быть нулевым")
    @Min(value = -273, message = "Уровень температуры по шкале Цельсия не может быть ниже -273")
    @Max(value = 500, message = "Уровень температуры по шкале Цельсия не может быть выше 500")
    private Integer temperatureC;

    @NotNull(message = "Влажность не может быть нулевой")
    @Min(value = 0, message = "Влажность не может быть ниже 0")
    @Max(value = 100, message = "Влажность не может быть выше 100")
    private Integer humidity;

    @NotNull(message = "Уровень CO2 не может быть нулевым")
    @Min(value = 0, message = "Уровень CO2 не может быть ниже 0")
    @Max(value = 100_000, message = "Уровень CO2 не может быть выше 100.000")
    private Integer co2Level;

    @Override
    public SensorEventType getType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }
}
