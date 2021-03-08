package projeto.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.security.auth.Subject;
import java.io.Serializable;
import java.security.Principal;


@Getter
@Setter
public class AuthUser implements Principal, Serializable {

    private String username;
    private String role;

    public AuthUser(String username, String role) {
        this.username = username;
        this.role = role;
    }

    @Override
    public String getName() {
        return username;
    }


}
