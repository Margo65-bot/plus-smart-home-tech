package dto.sensor;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class MotionSensorEvent extends SensorEvent {
    @NotNull(message = "Качество связи не может быть нулевым")
    @Min(value = 0, message = "Качество связи не может быть ниже 0")
    @Max(value = 100, message = "Качество связи не может быть выше 100")
    private Integer linkQuality;

    @NotNull(message = "Наличие/отсутствие движения не может быть нулевым")
    private Boolean motion;

    @NotNull(message = "Напряжение не может быть нулевым")
    @Min(value = 0, message = "Напряжение не может быть ниже 0")
    @Max(value = 500, message = "Напряжение не может быть выше 500")
    private Integer voltage;

    @Override
    public SensorEventType getType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }
}
