package projeto.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class NodeDurationAndMillisDTO extends NodeDurationDTO
{
    private long durationMillis;

    public NodeDurationAndMillisDTO( int node, long durationMillis )
    {
        super( node, new DurationDTO( durationMillis ) );
        this.durationMillis = durationMillis;
    }

    public NodeDurationAndMillisDTO( int node, DurationDTO durationDTO )
    {
        super( node, durationDTO );
        this.durationMillis = durationDTO.toMillis();
    }

    public static List<NodeDurationAndMillisDTO> convertFromDurationsDTO(List<NodeDurationDTO> original )
    {
        List<NodeDurationAndMillisDTO> res = new ArrayList<>();

        for ( NodeDurationDTO nodeDuration : original )
        {
            res.add( new NodeDurationAndMillisDTO(
                    nodeDuration.getNode(),
                    nodeDuration.getDuration() ) );
        }

        return res;

    }


}

