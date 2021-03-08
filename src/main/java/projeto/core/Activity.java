package projeto.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "ACTIVITIES")

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Activity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    private String name;
    private String description;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Workstation> workstations;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Event> events;

    @OneToMany(mappedBy = "activity")
    List<ActivityUserEntry> entries;

    public Activity(@NotNull String name, String description) {
        this.name = name;
        this.description = description;
        this.workstations = new ArrayList<>();
        this.events = new ArrayList<>();
        this.entries = new ArrayList<>();
    }

}
