package projeto.api.dtos.resources.operationalhours;

import lombok.Getter;
import lombok.Setter;
import projeto.api.dtos.DurationDTO;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class ActivityTotalOperationalHoursDTO {
    private String activity;
    private DurationDTO totalOperationalHours;
    private long totalOperationalHoursMillis;

    public ActivityTotalOperationalHoursDTO(String activity, long totalOperationalHoursMillis) {
        this.activity = activity;
        this.totalOperationalHoursMillis = totalOperationalHoursMillis;
        this.totalOperationalHours = new DurationDTO( totalOperationalHoursMillis );
    }
}
