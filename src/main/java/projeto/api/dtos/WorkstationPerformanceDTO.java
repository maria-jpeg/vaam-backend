package projeto.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class WorkstationPerformanceDTO extends FrequencyDurationDTO {

    private String workstation;
    private int frequency;

    public WorkstationPerformanceDTO(String workstation, int frequency, DurationDTO meanDuration, DurationDTO medianDuration, DurationDTO minDuration, DurationDTO maxDuration)
    {
        super(frequency, meanDuration, medianDuration, minDuration, maxDuration);
        this.workstation = workstation;
        this.frequency = frequency;
    }

}
