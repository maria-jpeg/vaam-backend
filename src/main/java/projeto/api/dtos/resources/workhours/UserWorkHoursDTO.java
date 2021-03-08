package projeto.api.dtos.resources.workhours;

import lombok.Getter;
import lombok.Setter;
import projeto.api.dtos.DurationDTO;

@Getter
@Setter
public class UserWorkHoursDTO {
    private String username;
    private long workHoursMillis;
    private DurationDTO workHours;

    public UserWorkHoursDTO(String username, long workHoursMillis) {
        this.username = username;
        this.workHoursMillis = workHoursMillis;
        this.workHours = new DurationDTO( workHoursMillis );
    }
}
