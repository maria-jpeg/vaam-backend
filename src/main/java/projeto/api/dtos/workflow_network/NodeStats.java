package projeto.api.dtos.workflow_network;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import projeto.api.dtos.DurationDTO;
import projeto.api.dtos.FrequencyDurationDTO;


@NoArgsConstructor
@Getter @Setter
public class NodeStats extends FrequencyDurationDTO
{
    private int node;

    public NodeStats(int node,
                     int frequency, DurationDTO meanDuration, DurationDTO medianDuration, DurationDTO minDuration, DurationDTO maxDuration)
    {
        super( frequency, meanDuration, medianDuration, minDuration, maxDuration );
        this.node = node;
    }
}
