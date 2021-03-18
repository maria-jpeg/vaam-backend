package projeto.api.dtos.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import projeto.core.Activity;
import projeto.core.User;
import projeto.core.Workstation;

//Mostrar dados relativos às atividades realizadas por cada user e workstation #sprint2
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActivityUserEntryDTO
{
    private UserDTO user;
    private ActivityDTO activity;
    private String startDate;
    private String endDate;
    private WorkstationDTO workstation;
    private long timeSpentMillis;

    public ActivityUserEntryDTO(UserDTO user, ActivityDTO activity, DateTime startDate, DateTime endDate, WorkstationDTO workstation)
    {
        this.user = user;
        this.activity = activity;
        this.startDate = startDate.toString( "dd-MM-yyyy HH:mm:ss.SSS" );
        this.endDate = endDate.toString( "dd-MM-yyyy HH:mm:ss.SSS" );
        this.workstation = workstation;
        Duration duration = new Duration(startDate,endDate);
        this.timeSpentMillis = duration.getMillis();
    }
}
