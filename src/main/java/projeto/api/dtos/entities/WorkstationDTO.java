package projeto.api.dtos.entities;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import projeto.api.dtos.DTO;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkstationDTO implements DTO {
    private long id;
    private String name;
    private long activityId;
    private Boolean isTagging;
    private Boolean isEndWorkstation;

    public WorkstationDTO(long id, String name, Boolean isTagging, Boolean isEndWorkstation)
    {
        this.id = id;
        this.name = name;
        this.isTagging = isTagging;
        this.isEndWorkstation = isEndWorkstation;
    }

    @Override
    public void reset() {
        setId(0);
        setName(null);
        setActivityId(0);
        setIsTagging(null);
        setIsEndWorkstation(null);
    }
}
