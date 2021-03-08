package projeto.api.dtos.conformance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter @Setter
@AllArgsConstructor
public class FiltersDTO {

    private List<String> activities;
    private List<String> resources;
    private List<String> moulds;
    private List<String> parts;

    public FiltersDTO(List<String> activities, List<String> resources, List<String> cases, boolean isSubProcess) {
        this.activities = activities;
        this.resources = resources;
        if(!isSubProcess){
            this.moulds = cases;
        }else{
            this.parts = cases;
        }
    }
}
