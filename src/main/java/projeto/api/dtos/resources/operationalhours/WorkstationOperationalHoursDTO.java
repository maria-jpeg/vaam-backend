package projeto.api.dtos.resources.operationalhours;

import lombok.Getter;
import lombok.Setter;
import projeto.api.dtos.DurationDTO;

@Getter
@Setter
public class WorkstationOperationalHoursDTO {
    private String workstationName;
    private long operationalHoursMillis;
    private DurationDTO operationalHours;

    public WorkstationOperationalHoursDTO(String workstationName, long operationalHoursMillis) {
        this.workstationName = workstationName;
        this.operationalHoursMillis = operationalHoursMillis;
        this.operationalHours = new DurationDTO( operationalHoursMillis );
    }
}
