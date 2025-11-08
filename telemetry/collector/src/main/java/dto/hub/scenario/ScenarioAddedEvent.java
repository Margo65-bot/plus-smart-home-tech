package dto.hub.scenario;

import dto.hub.HubEvent;
import dto.hub.HubEventType;
import dto.hub.device.DeviceAction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString(callSuper = true)
public class ScenarioAddedEvent extends HubEvent {
    @NotBlank(message = "Название добавленного сценария не должно быть пустым")
    @Size(min = 3, max = 214748364, message = "Название добавленного сценария должно содержать не менее 3 символов")
    private String name;

    @NotNull(message = "Список условий, которые связаны со сценарием не может быть пустым")
    @NotEmpty(message = "Список условий, которые связаны со сценарием не может быть пустым")
    private List<ScenarioCondition> conditions;

    @NotNull(message = "Список действий, которые должны быть выполнены в рамках сценария не может быть пустым")
    @NotEmpty(message = "Список действий, которые должны быть выполнены в рамках сценария не может быть пустым")
    private List<DeviceAction> actions;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }
}
