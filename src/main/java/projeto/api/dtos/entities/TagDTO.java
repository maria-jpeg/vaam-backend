package projeto.api.dtos.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import projeto.api.dtos.DTO;

import javax.persistence.Id;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class TagDTO implements DTO {

    @Id
    private String rfid;
    private boolean isAvailable;
    private boolean isUser;

    @Override
    public void reset() {
        setRfid( null );
        setAvailable( true );
        setUser( false );
    }
}
