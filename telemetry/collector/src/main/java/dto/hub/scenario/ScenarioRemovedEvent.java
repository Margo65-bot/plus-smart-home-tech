package dto.hub.scenario;

import dto.hub.HubEvent;
import dto.hub.HubEventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class ScenarioRemovedEvent extends HubEvent {
    @NotBlank(message = "Название удаленного сценария не должно быть пустым")
    @Size(min = 3, max = 214748364, message = "Название удаленного сценария должно содержать не менее 3 символов")
    private String name;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_REMOVED;
    }
}
