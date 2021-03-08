package projeto.api.dtos.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import projeto.api.dtos.DTO;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WorkstationDTO implements DTO {
    private long id;
    private String name;
    private long activityId;
    private Boolean isTagging;
    private Boolean isEndWorkstation;

    @Override
    public void reset() {
        setId(0);
        setName(null);
        setActivityId(0);
        setIsTagging(null);
        setIsEndWorkstation(null);
    }
}
