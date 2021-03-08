package projeto.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "LOGS")
@NamedQueries({
        @NamedQuery(name = "Log.all",
                query = "SELECT l FROM Log l"),
        @NamedQuery(name = "Log.getActivities",
                query = "SELECT DISTINCT e.activity.name FROM Event e"), //estas queries foram alteradas par anão dar erro em relação às novas colunas da tabela Events
        @NamedQuery(name = "Log.getResources",
                query = "SELECT DISTINCT e FROM Event e")
})

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class Log implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String fileName;
    private String description;

    private DateTime startDate;
    private DateTime endDate;
    private int numberOfCases;
    private int numberOfActivities;

    public Log( String fileName, String description )
    {
        this.fileName = fileName;
        this.description = description;
    }
}
