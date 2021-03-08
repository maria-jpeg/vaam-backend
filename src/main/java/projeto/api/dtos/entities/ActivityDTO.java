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
public class ActivityDTO implements DTO {

    private long id;
    private String name;
    private String description;


    @Override
    public void reset() {
        setId(0);
        setName(null);
        setDescription(null);
    }
}
