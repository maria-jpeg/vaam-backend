package projeto.api.dtos.resources;

import lombok.Getter;
import lombok.Setter;
import projeto.api.dtos.DurationDTO;
import projeto.controller.PartBean;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter @Setter
public class ResourcesActivityDTO
{
    private String activity;
    private DurationDTO mean;
    private long meanMillis;
    private List<ResourceActivityPerformanceDTO> resources;
    private Set<String> moulds;
    private Set<String> parts;

    public ResourcesActivityDTO(String activity, long meanMillis, List<ResourceActivityPerformanceDTO> resources, Set<String> cases, boolean isSubProcess, PartBean partBean)
    {
        this.activity = activity;
        this.meanMillis = meanMillis;
        this.mean = new DurationDTO( meanMillis );
        this.resources = resources;
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
