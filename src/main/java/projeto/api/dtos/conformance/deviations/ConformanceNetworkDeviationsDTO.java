package projeto.api.dtos.conformance.deviations;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import projeto.algorithms_process_mining.FootprintMatrix;
import projeto.api.dtos.workflow_network.AbstractWorkflowNetworkStringNodeDTO;
import projeto.core.Relation;

import java.util.List;

@NoArgsConstructor
@Getter @Setter
public class ConformanceNetworkDeviationsDTO extends AbstractWorkflowNetworkStringNodeDTO< NodeRelationDeviationsMap >
{
    protected List<Relation> deviations;

    public ConformanceNetworkDeviationsDTO( FootprintMatrix footprint, List<NodeRelationDeviationsMap> relations, List<Relation> deviations)
    {
        super( footprint );

        this.relations = relations;
        this.deviations = deviations;
    }
}
