package projeto.api.dtos.conformance.deviations;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import projeto.api.dtos.NodeFrequencyDTO;
import projeto.api.dtos.workflow_network.RelationMapDTO;

import java.util.List;


@NoArgsConstructor
@Getter @Setter
public class NodeRelationDeviationsMap extends RelationMapDTO<NodeFrequencyDTO>
{
    private int totalFrequency;

    public NodeRelationDeviationsMap(int from, int totalFrequency, List<NodeFrequencyDTO> to )
    {
        super(from, to);
        this.totalFrequency = totalFrequency;
    }
}

