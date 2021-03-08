package projeto.api.dtos.entities;

import lombok.Getter;
import lombok.Setter;
import projeto.api.dtos.DTO;

@Getter @Setter
public class UserDTO implements DTO {

    private String username;
    private String password;
    private String role;
    private String name;
    private String email;

    public UserDTO() {
    }

    public UserDTO(String username, String password, String role, String name, String email) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.name = name;
        this.email = email;
    }

    public UserDTO(String username, String role, String name, String email) {
        this.username = username;
        this.role = role;
        this.name = name;
        this.email = email;
    }

    @Override
    public void reset() {
        setUsername( null );
        setPassword( null );
        setRole( null );
        setName(null);
        setEmail(null);
    }
}
