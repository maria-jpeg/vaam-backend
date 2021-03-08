package projeto.api.dtos.resources.operationalhours;

import lombok.Getter;
import lombok.Setter;
import projeto.api.dtos.DurationDTO;
import projeto.api.dtos.resources.workhours.UserWorkHoursDTO;
import projeto.controller.PartBean;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class ActivityWorkstationsDTO {
    private String activity;
    private DurationDTO totalOperationalHours;
    private long totalOperationalHoursMillis;
    private List<WorkstationOperationalHoursDTO> workstations;
    private Set<String> moulds;
    private Set<String> parts;

    public ActivityWorkstationsDTO(String activity, long totalOperationalHoursMillis, List<WorkstationOperationalHoursDTO> workstations, Set<String> cases, boolean isSubProcess, PartBean partBean) {
        this.activity = activity;
        this.totalOperationalHoursMillis = totalOperationalHoursMillis;
        this.totalOperationalHours = new DurationDTO( totalOperationalHoursMillis );
        this.workstations = workstations;
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
