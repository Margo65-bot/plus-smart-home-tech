package dto.hub.device;

import dto.hub.HubEvent;
import dto.hub.HubEventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class DeviceAddedEvent extends HubEvent {
    @NotBlank(message = "Идентификатор добавленного устройства не может быть пустым")
    private String id;

    @NotNull(message = "Перечисление типов устройств, которые могут быть добавлены в систему, не может быть нулевым")
    private DeviceType deviceType;

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_ADDED;
    }
}
