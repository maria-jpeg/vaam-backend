package projeto.api.dtos.entities;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;
import projeto.api.dtos.DTO;
import projeto.core.Activity;
import projeto.core.Mould;
import projeto.core.Part;
import java.util.List;

import java.util.LinkedList;


@NoArgsConstructor
@Getter @Setter
public class EventDTO implements DTO
{

    private long id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long activityId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long processId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String mouldCode;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String partCode;
    private String startDate;
    private String endDate;
    private long duration;
    private Boolean isEstimatedEnd;
    private MouldDTO mould;
    private PartDTO part;
    private ProcessDTO process;
    private ActivityDTO activity;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ActivityUserEntryDTO> activityUserEntry;

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
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
        this.isEstimatedEnd = isEstimatedEnd;
        this.mould = mould;
        this.part = part;
        this.activity = activity;
        this.process = process;
        this.id = id;
    }

    //EventDTO que também contém os moldes, partes e activityUserEntry
    public EventDTO(ActivityDTO activity, ProcessDTO process, MouldDTO mould, PartDTO part, String startDate, String endDate, long duration, Boolean isEstimatedEnd, long id, List<ActivityUserEntryDTO> activityUserEntryDTOList)
    {
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
        this.isEstimatedEnd = isEstimatedEnd;
        this.mould = mould;
        this.part = part;
        this.activity = activity;
        this.process = process;
        this.id = id;
        this.activityUserEntry = activityUserEntryDTOList;
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
