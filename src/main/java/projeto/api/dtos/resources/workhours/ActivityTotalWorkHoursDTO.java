package projeto.api.dtos.resources.workhours;

import lombok.Getter;
import lombok.Setter;
import projeto.api.dtos.DurationDTO;

@Getter
@Setter
public class ActivityTotalWorkHoursDTO {
    private String activity;
    private DurationDTO totalWorkHours;
    private long totalWorkHoursMillis;

    public ActivityTotalWorkHoursDTO(String activity, long totalWorkHoursMillis) {
        this.activity = activity;
        this.totalWorkHoursMillis = totalWorkHoursMillis;
        this.totalWorkHours = new DurationDTO( totalWorkHoursMillis );
    }
}
