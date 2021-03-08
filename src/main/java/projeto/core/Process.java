package projeto.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PROCESSES")
@NamedQueries({
        @NamedQuery(name = "Process.all",
                query = "SELECT p FROM Process p ORDER BY p.id"),
        @NamedQuery(name = "Process.checkId",
                query = "SELECT p FROM Process p WHERE p.id = :id"),
        @NamedQuery(name = "Process.getActivities",
                query = "SELECT DISTINCT e.activity FROM Event e WHERE e.process.id = :processId"),
        @NamedQuery(name = "Process.getUsers",
                query = "SELECT au FROM ActivityUserEntry au WHERE ( au.startDate <= :processEndDate AND au.endDate >= :processStartDate)"),
        @NamedQuery(name = "Process.getWorkstations",
                query = "SELECT DISTINCT e.workstation FROM Event e WHERE e.process.id = :processId"),
        @NamedQuery(
                name = "Process.getProcessesByName",
                query = "SELECT p FROM Process p WHERE p.name  = :name ORDER BY p.id"
        )
})

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Process implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    private String name;
    private String description;
    private DateTime startDate;
    private DateTime endDate;
    private int numberOfCases;
    private int numberOfActivities;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parentProcess", referencedColumnName = "id")
    private Process parentProcess;

    @OneToOne(mappedBy = "parentProcess")
    private Process subProcess;

    @OneToMany(mappedBy = "process", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Event> events;

    @OneToMany(mappedBy = "process", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Part> parts;

    @OneToMany(mappedBy = "process", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Mould> moulds;

    public Process(@NotNull String name, String description) {
        this.name = name;
        this.description = description;
        this.events = new ArrayList<>();
        this.parts = new ArrayList<>();
        this.moulds = new ArrayList<>();
    }

    public Process(String name, String description, DateTime startDate, DateTime endDate, int numberOfCases, int numberOfActivities, Process parentProcess) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfCases = numberOfCases;
        this.numberOfActivities = numberOfActivities;
        this.parentProcess = parentProcess;
        this.events = new ArrayList<>();
        this.parts = new ArrayList<>();
        this.moulds = new ArrayList<>();
    }
}
