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
@Table(name = "WORKSTATIONS")
@NamedQueries({
        @NamedQuery(
                name = "Workstation.getWorkstationsByName",
                query = "SELECT w FROM Workstation w WHERE w.name  = :name ORDER BY w.id"
        ),
        @NamedQuery(
                name = "Workstation.getTaggingWorkstations",
                query = "SELECT w FROM Workstation w WHERE w.isTagging = true"
        )
})

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Workstation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    private String name;

    private Boolean isTagging;
    private Boolean isEndWorkstation;

    @ManyToOne
    @JoinColumn(name = "activityId")
    private Activity activity;

    @OneToMany(mappedBy = "workstation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Event> events;

    @OneToMany(mappedBy = "workstation")
    List<ActivityUserEntry> entries;

    public Workstation(@NotNull String name, Boolean isTagging, Boolean isEndWorkstation, Activity activity) {
        this.name = name;
        this.isTagging = isTagging;
        this.isEndWorkstation = isEndWorkstation;
        this.activity = activity;
        this.events = new ArrayList<>();
        this.entries = new ArrayList<>();
    }

    public Workstation(long id, @NotNull String name, Boolean isTagging, Boolean isEndWorkstation, Activity activity) {
        this.id = id;
        this.name = name;
        this.isTagging = isTagging;
        this.isEndWorkstation = isEndWorkstation;
        this.activity = activity;
        this.events = new ArrayList<>();
        this.entries = new ArrayList<>();
    }
}
