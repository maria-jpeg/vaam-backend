package projeto.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PARTS")
@NamedQueries({
        @NamedQuery(name = "Part.all",
                query = "SELECT p FROM Part p ORDER BY p.code"),
        @NamedQuery(name = "Mould.getPartsByMouldCode",
                query = "SELECT p FROM Part p WHERE p.mould.code = :mouldCode"),
        @NamedQuery(name = "Part.getPartsWithTag",
                query = "SELECT p FROM Part p WHERE p.tag.rfid = :tagRfid"),
        @NamedQuery(name = "Part.getPartMouldCode",
                query = "SELECT m.code FROM Mould m JOIN Part p ON p.mould.code = m.code WHERE p.code = :partCode")
})

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Part implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String code;
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tagRfid", referencedColumnName = "rfid")
    private Tag tag;

    @ManyToOne
    @JoinColumn(name = "mouldCode")
    private Mould mould;
    @ManyToOne
    @JoinColumn(name = "processId")
    private Process process;

    @OneToMany(mappedBy = "part", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Event> events;


}
