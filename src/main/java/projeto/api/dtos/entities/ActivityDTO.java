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
public class ActivityDTO implements DTO {

    private long id;
    private String name;
    private String description;
    private EventDTO eventDTO;

    public ActivityDTO(long id, String name, String description)
    {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    @Override
    public void reset() {
        setId(0);
        setName(null);
        setDescription(null);
    }
}
