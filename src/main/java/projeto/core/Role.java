package projeto.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "ROLES")
@NamedQueries({

        @NamedQuery(
                name = "Role.getUserRoles",
                query = "SELECT r.name FROM Role r"
        )
})

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Role implements Serializable {
    @Id
    private String name;
    private String description;

    @OneToMany(mappedBy = "role")
    private List<User> users;
}
