package projeto.api.dtos.resources.workhours;

import lombok.Getter;
import lombok.Setter;
import projeto.api.dtos.DurationDTO;
import projeto.controller.PartBean;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class ActivityUsersDTO {
    private String activity;
    private DurationDTO totalWorkHours;
    private long totalWorkHoursMillis;
    private List<UserWorkHoursDTO> users;
    private Set<String> moulds;
    private Set<String> parts;

    public ActivityUsersDTO(String activity, long totalWorkHoursMillis, List<UserWorkHoursDTO> users, Set<String> cases, boolean isSubProcess, PartBean partBean) {
        this.activity = activity;
        this.totalWorkHoursMillis = totalWorkHoursMillis;
        this.totalWorkHours = new DurationDTO( totalWorkHoursMillis );
        this.users = users;
        if(!isSubProcess){
            this.moulds = cases;
            this.parts = null;
        }else{
            this.parts = cases;

            Set<String> partsMoulds = new HashSet<>();
            for (String partCode : cases) {
                String mouldCode = partBean.getPartMouldCode(partCode);
                partsMoulds.add(mouldCode);
            }

            this.moulds = partsMoulds;
        }
    }
}
