package projeto.api.dtos.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;
import projeto.api.dtos.DTO;
import projeto.core.Activity;
import projeto.core.Mould;
import projeto.core.Part;

import java.io.Serializable;

@NoArgsConstructor
@Getter @Setter
public class EventDTO implements DTO
{

    private long id;
    private long activityId;
    private long processId;
    private String mouldCode;
    private String partCode;
    private String startDate;
    private String endDate;
    private long duration;
    private Boolean isEstimatedEnd;
    private MouldDTO mould;
    private PartDTO part;
    private ProcessDTO process;
    private ActivityDTO activity;

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

    //EventDTO que também contém os moldes e partes
    public EventDTO(ActivityDTO activity, ProcessDTO process, MouldDTO mould, PartDTO part, String startDate, String endDate, long duration, Boolean isEstimatedEnd, long id)
    {
        this.activityId = activity.getId();
        this.processId = process.getId();
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
        this.isEstimatedEnd = isEstimatedEnd;
        this.mould = mould;
        this.part = part;
        this.mouldCode = mould.getCode();
        if (part != null)
            this.partCode = part.getCode();
        this.id = id;
        this.activity = activity;
        this.process = process;
    }

    //@Override
    public void reset()
    {
        setStartDate( null );
        setEndDate( null );
        setDuration(0);
        setIsEstimatedEnd(null);
    }

}
