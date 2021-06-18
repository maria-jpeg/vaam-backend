package projeto.api.dtos.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;
import projeto.api.dtos.DTO;

@NoArgsConstructor
@Getter
@Setter
public class DashboardDTO implements DTO {

    private long id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String date;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int value;
    @JsonInclude(JsonInclude.Include.NON_NULL)//Todo Mudar para enum
    private String unit;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ProcessDTO process;

    public DashboardDTO(long id,DateTime date, int value, String unit, String description, ProcessDTO process) {
        this.id = id;
        this.date = date.toString( "dd-MM-yyyy HH:mm:ss.SSS" );
        this.value = value;
        this.unit = unit;
        this.description = description;
        this.process = process;
    }

    public DashboardDTO(long id,DateTime date, int value, String unit, String description) {
        this.id = id;
        this.date = date.toString( "dd-MM-yyyy HH:mm:ss.SSS" );
        this.value = value;
        this.unit = unit;
        this.description = description;
    }

    //@Override
    public void reset() {

    }
}
