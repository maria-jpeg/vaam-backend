package projeto.api.dtos.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import projeto.api.dtos.DTO;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO implements DTO {

    private String name;
    private String description;

    @Override
    public void reset() {
        setName( null );
        setDescription( null );
    }
}
