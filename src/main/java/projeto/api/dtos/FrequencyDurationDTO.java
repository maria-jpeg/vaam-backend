package projeto.api.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public abstract class FrequencyDurationDTO {

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int frequency;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private DurationDTO meanDuration;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private DurationDTO medianDuration;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private DurationDTO minDuration;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private DurationDTO maxDuration;

}
