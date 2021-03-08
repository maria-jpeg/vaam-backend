package projeto.api.dtos.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;
import projeto.api.dtos.DTO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProcessDTO implements DTO {

    private long id;
    @NotNull( message = "- name required")
    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private int numberOfCases;
    private int numberOfActivities;

    public ProcessDTO(long id, String name, String description, DateTime startDate, DateTime endDate, int numberOfCases, int numberOfActivities) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate != null ? startDate.toString( "dd-MM-yyyy HH:mm" ) : null;
        this.endDate = endDate != null ? endDate.toString( "dd-MM-yyyy HH:mm" ) : null;
        this.numberOfCases = numberOfCases;
        this.numberOfActivities = numberOfActivities;
    }

    @Override
    public void reset() {
        setId( 0 );
        setName( null );
        setDescription( null );
        setStartDate( null );
        setEndDate( null );
        setNumberOfCases( 0 );
        setNumberOfActivities( 0 );
    }
}
