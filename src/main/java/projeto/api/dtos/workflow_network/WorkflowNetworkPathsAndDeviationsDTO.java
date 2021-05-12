package projeto.api.dtos.workflow_network;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import projeto.algorithms_process_mining.FootprintMatrix;
import projeto.algorithms_process_mining.inductive_miner.FootprintInductive;
import projeto.algorithms_process_mining.inductive_miner.InductiveMiner;
import projeto.api.dtos.conformance.deviations.NodeRelationDeviationsMap;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class WorkflowNetworkPathsAndDeviationsDTO extends WorkflowNetworkDTO {

    private List<NodeRelationDeviationsMap> relationDeviation;

    public WorkflowNetworkPathsAndDeviationsDTO(FootprintInductive footprint) {
        super(footprint);

        this.relationDeviation = footprint.getIvm().getDeviations();
    }
}
