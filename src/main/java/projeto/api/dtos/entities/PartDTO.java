package projeto.api.dtos.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import projeto.api.dtos.DTO;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class PartDTO implements DTO {

    private String code;
    private String description;
    private String tagRfid;

    @Override
    public void reset() {
        setCode( null );
        setDescription( null );
    }
}
