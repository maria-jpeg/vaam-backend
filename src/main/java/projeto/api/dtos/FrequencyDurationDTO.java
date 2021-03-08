package projeto.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public abstract class FrequencyDurationDTO {

    private int frequency;
    private DurationDTO meanDuration;
    private DurationDTO medianDuration;
    private DurationDTO minDuration;
    private DurationDTO maxDuration;

}
