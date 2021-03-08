package projeto.core.extra;


import lombok.Getter;
import org.joda.time.DateTime;
import projeto.api.dtos.compare.FilterInputWrapperDTO;
import projeto.controller.Utils;

import java.util.ArrayList;
import java.util.List;

@Getter
public class FilterWrapper
{
    //private List<Long> cases;
    private List<String> moulds;
    private List<String> parts;
    private DateTime startDate;
    private DateTime endDate;
    private ArrayList<String> activities;
    private ArrayList<String> resources;
    private Boolean isEstimatedEnd;

    //private boolean casesFilter;
    private boolean mouldsFilter;
    private boolean partsFilter;
    private boolean startDateFilter;
    private boolean endDateFilter;
    private boolean activitiesFilter;
    private boolean resourcesFilter;
    private boolean estimatedEndFilter;

    public FilterWrapper( FilterInputWrapperDTO dto ) throws IllegalArgumentException
    {
        if( dto == null )
            throw new IllegalArgumentException( "FilterInputWrapperDTO dto cant be null" );

        this.moulds = dto.getMoulds();
        this.parts = dto.getParts();

        Pair<DateTime, DateTime> datesPair = parseDateBetween( dto.getStartDate(), dto.getEndDate() );
        this.startDate = datesPair.getFirst();
        this.endDate = datesPair.getSecond();

        this.activities = dto.getActivities();
        this.resources = dto.getResources();
        this.isEstimatedEnd = dto.getIsEstimatedEnd();

        // Filter tags
        //this.casesFilter = this.cases != null && !this.cases.isEmpty();
        this.mouldsFilter = this.moulds != null && !this.moulds.isEmpty();
        this.partsFilter = this.parts != null && !this.parts.isEmpty();
        this.startDateFilter = this.startDate != null;
        this.endDateFilter = this.endDate != null;
        this.activitiesFilter = this.activities != null && !this.activities.isEmpty();
        this.resourcesFilter = this.resources != null && !this.resources.isEmpty();
        this.estimatedEndFilter = this.isEstimatedEnd != null && this.isEstimatedEnd == false; //se vier a false, vamos filtrar/retirar as instâncias que têm o isEstimatedEnd a 1

    }

    private Pair<DateTime,DateTime> parseDateBetween(String startDateStr, String endDateStr ) throws IllegalArgumentException
    {

        DateTime startDate = startDateStr == null ? null :
                Utils.parseDate( startDateStr )
                        .hourOfDay().setCopy( 0 )
                        .minuteOfHour().setCopy( 0 );

        DateTime endDate = endDateStr == null ? null :
                Utils.parseDate( endDateStr )
                        .hourOfDay().setCopy( 23 )
                        .minuteOfHour().setCopy( 59 );

        return new Pair<>( startDate, endDate );

    }


    public static class FilterStatus
    {
        public boolean startDateMatch = true;
        public boolean endDateMatch = true;
        public boolean hasActivity = false;
        public boolean hasResource = false;
        public boolean hasEstimatedEnd = false;

        public FilterStatus() {}

        public boolean isConform( FilterWrapper filter )
        {
            if( filter.isStartDateFilter() && !startDateMatch )
                return false;

            if( filter.isEndDateFilter() && !endDateMatch )
                return false;

            if( filter.isActivitiesFilter() && !hasActivity )
                return false;

            if( filter.isResourcesFilter() && !hasResource )
                return false;

            if( filter.isEstimatedEndFilter() && hasEstimatedEnd )
                return false;

            return true;
        }
    }

}