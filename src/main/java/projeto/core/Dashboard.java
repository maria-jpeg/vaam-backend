package projeto.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "DASHBOARD")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Dashboard implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private DateTime date;
    private int value;
    private String unit;
    private String description;

    @ManyToOne
    @JoinColumn(name = "processId")
    private Process process;

    public Dashboard(int value, String unit, String description, Process process) {
        this.date = DateTime.now();
        this.value = value;
        this.unit = unit;
        this.description = description;
        this.process = process;
    }
}
