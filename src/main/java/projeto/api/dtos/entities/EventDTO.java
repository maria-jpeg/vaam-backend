package projeto.api.dtos.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;
import projeto.api.dtos.DTO;

@NoArgsConstructor
@Getter @Setter
public class EventDTO implements DTO {

    private long id;
    private long activityId;
    private long processId;
    private String mouldCode;
    private String partCode;
    private String startDate;
    private String endDate;
    private long duration;
    private Boolean isEstimatedEnd;

    public EventDTO(long activityId, long processId, String mouldCode, String partCode, String startDate, String endDate, long duration, Boolean isEstimatedEnd, long id) {
        this.activityId = activityId;
        this.processId = processId;
        this.mouldCode = mouldCode;
        this.partCode = partCode;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
        this.isEstimatedEnd = isEstimatedEnd;
        this.id = id;
    }

    public EventDTO(long activityId, long processId, String mouldCode, String startDate, String endDate, long duration, Boolean isEstimatedEnd, long id) {
        this.activityId = activityId;
        this.processId = processId;
        this.mouldCode = mouldCode;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
        this.isEstimatedEnd = isEstimatedEnd;
        this.id = id;
    }

    @Override
    public void reset()
    {
        setStartDate( null );
        setEndDate( null );
        setDuration(0);
        setIsEstimatedEnd(null);
    }

}
