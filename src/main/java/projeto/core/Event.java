package projeto.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;
import projeto.controller.Utils;
import projeto.core.extra.Pair;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "EVENTS")
@NamedQueries({
        @NamedQuery(name = "Process.getEventsByProcessID",
                query = "SELECT e FROM Event e WHERE e.process.id = :processId"),
        @NamedQuery(name = "Process.getMouldCodesFromProcessId",
                query = "SELECT DISTINCT e.mould.code FROM Event e WHERE e.process.id = :processId"),
        @NamedQuery(name = "Process.getPartCodesFromProcessId",
                query = "SELECT DISTINCT e.part.code FROM Event e WHERE e.process.id = :processId"),
        @NamedQuery(name = "Event.getMoulds",
                query = "SELECT e FROM Event e WHERE e.process.id = :processId AND e.mould.code in (:moulds)"),
        @NamedQuery(name = "Event.getParts",
                query = "SELECT e FROM Event e WHERE e.process.id = :processId AND e.part.code in (:parts)"),
        @NamedQuery(name = "Process.getNumberOfActivities",
                query = "SELECT DISTINCT e.activity.id  FROM Event e WHERE e.process.id = :processId"),
        @NamedQuery(name = "Process.getNumberOfCases",
                query = "SELECT DISTINCT e.mould.code FROM Event e WHERE e.process.id = :processId")
})

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class Event implements Serializable
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private DateTime startDate;
    private DateTime endDate;
    private long duration;

    //ACRESCENTADOS NA SPRINT3:
    @ManyToOne
    @JoinColumn(name = "partCode")
    private Part part;
    @ManyToOne
    @JoinColumn(name = "mouldCode")
    private Mould mould;
    @ManyToOne
    @JoinColumn(name = "processId")
    private Process process;
    @ManyToOne
    @JoinColumn(name = "activityId")
    private Activity activity;

    private Boolean isEstimatedEnd;

    @ManyToOne
    @JoinColumn(name = "workstationId")
    private Workstation workstation;

    public Event(Activity activity, Process process, Mould mould, Part part, String startDate, String endDate, Boolean isEstimatedEnd)
    {
        Pair<DateTime, DateTime> datesPair = parseDateBetween( startDate, endDate );

        if( startDate == null && this.getStartDate() == null){
            //throw new IllegalArgumentException( "Start date cant be null." );
            this.startDate = DateTime.now();
        }else{
            this.startDate = datesPair.getFirst();
        }

        this.endDate = datesPair.getSecond();

        if( endDate != null )
            this.duration = this.endDate.getMillis() - this.startDate.getMillis();

        this.activity = activity;
        this.process = process;
        this.mould = mould;
        this.part = part;
        this.isEstimatedEnd = isEstimatedEnd;
    }

    private Pair<DateTime,DateTime> parseDateBetween(String startDateStr, String endDateStr ) throws IllegalArgumentException
    {

        DateTime startDate = startDateStr == null ? null :
                Utils.parseDate( startDateStr );

        DateTime endDate = endDateStr == null ? null :
                Utils.parseDate( endDateStr );

        return new Pair<>( startDate, endDate );

    }

    public void setEndDate(String endDate) throws IllegalArgumentException {

        if( endDate == null )
            throw new IllegalArgumentException( "End time cant be null." );

        Pair<DateTime, DateTime> datesPair = parseDateBetween(null, endDate);
        this.endDate = datesPair.getSecond();

        if(this.endDate.getMillis() < this.startDate.getMillis()){
            throw new IllegalArgumentException( "End date must be after start date." );
        }

        this.duration = this.endDate.getMillis() - this.startDate.getMillis();
    }

}
