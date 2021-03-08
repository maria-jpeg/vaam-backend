package projeto.api.dtos.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import projeto.api.dtos.DTO;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class MouldDTO implements DTO {

    private String code;
    private String description;

    @Override
    public void reset() {
        setCode( null );
        setDescription( null );
    }
}
