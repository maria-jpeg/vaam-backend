package projeto.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MOULDS")
@NamedQueries({
        @NamedQuery(name = "Mould.all",
                query = "SELECT m FROM Mould m ORDER BY m.code")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Mould implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String code;
    private String description;

    @ManyToOne
    @JoinColumn(name = "processId")
    private Process process;

    @OneToMany(mappedBy = "mould", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Event> events;

    @OneToMany(mappedBy = "mould", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Part> parts;


}
