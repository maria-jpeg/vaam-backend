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
public class WorkstationUsersDTO {
    private String workstation;
    private DurationDTO workHours;
    private long totalWorkHoursMillis;
    private List<UserWorkHoursDTO> users;
    private Set<String> moulds;
    private Set<String> parts;

    public WorkstationUsersDTO(String workstation, long totalWorkHoursMillis, List<UserWorkHoursDTO> users, Set<String> cases, boolean isSubProcess, PartBean partBean) {
        this.workstation = workstation;
        this.totalWorkHoursMillis = totalWorkHoursMillis;
        this.workHours = new DurationDTO( totalWorkHoursMillis );
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
