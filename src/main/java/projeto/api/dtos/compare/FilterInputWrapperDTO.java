package projeto.api.dtos.compare;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class FilterInputWrapperDTO
{
    // Cases for the logs
    //protected List<Long> cases;

    //Moulds for the processes
    protected List<String> moulds;
    //Moulds for the subProcesses
    protected List<String> parts;

    //Dates
    protected String startDate;
    protected String endDate;

    // Activities ids
    protected ArrayList<String> activities;

    // Resources usernames
    protected ArrayList<String> resources;

    //include isEstimatedEnd instances?
    protected Boolean isEstimatedEnd;

}