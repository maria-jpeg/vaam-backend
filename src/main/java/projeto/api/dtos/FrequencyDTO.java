package projeto.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class FrequencyDTO extends FrequencyDurationDTO
{
    private String activity;
    private int frequency;
    private float relativeFrequency;

    public FrequencyDTO(String activity, int frequency, float relativeFrequency, DurationDTO meanDuration, DurationDTO medianDuration, DurationDTO minDuration, DurationDTO maxDuration)
    {
        super(frequency, meanDuration, medianDuration, minDuration, maxDuration);
        this.activity = activity;
        this.frequency = frequency;
        this.relativeFrequency = relativeFrequency;
    }
}
