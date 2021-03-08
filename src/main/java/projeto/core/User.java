package projeto.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import projeto.controller.Utils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.security.Principal;
import java.util.List;

@Entity
@Table(name = "USERS")
@NamedQueries({
        @NamedQuery(name = "User.all",
                query = "SELECT u FROM User u"),
        @NamedQuery(name = "User.checkUsername",
                query = "SELECT u FROM User u WHERE u.username = :username"),
        @NamedQuery(name = "User.checkCredentials",
                query = "SELECT u FROM User u WHERE u.username = :username AND u.password = :password"),
        @NamedQuery(
                name = "User.getUsersByEmail",
                query = "SELECT u FROM User u WHERE u.email  = :email ORDER BY u.username" // JPQL
        ),
        @NamedQuery(name = "User.getUsersWithTag",
                query = "SELECT u FROM User u WHERE u.tag.rfid = :tagRfid"),
        @NamedQuery(name = "User.getUserProcesses",
                query = "SELECT DISTINCT e.process FROM Event e JOIN ActivityUserEntry au ON e.activity.id = au.activity.id WHERE au.user.username = :username AND (au.startDate <= e.startDate AND au.endDate >= e.startDate)"),
        @NamedQuery(name = "User.getUserActivities",
                query = "SELECT DISTINCT au.activity FROM ActivityUserEntry au WHERE au.user.username = :username")
})

@NoArgsConstructor
@Getter @Setter
public class User implements Principal, Serializable {

    @Id
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String name;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role")
    private Role role;
    @NotNull
    @Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
            +"[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
            +"(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
            message = "{invalid.email}")
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rfid", referencedColumnName = "rfid")
    private Tag tag;

    @OneToMany(mappedBy = "user")
    List<ActivityUserEntry> entries;

    public User(String username, String password, Role role, String name, String email) {
        this.username = username;
        this.password = Utils.hashPassword(password);
        this.role = role;
        this.name = name;
        this.email = email;
    }

    @Override
    public String getName() {
        return username;
    }

    @Override
    public boolean equals(Object obj) {

        if( !(obj instanceof User) )
            return false;

        User user = (User)obj;

        return this.username == user.getUsername();
    }
}
