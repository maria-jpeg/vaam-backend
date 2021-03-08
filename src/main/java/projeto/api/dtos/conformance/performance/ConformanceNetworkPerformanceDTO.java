package projeto.api.dtos.conformance.performance;

import lombok.Getter;
import lombok.Setter;
import projeto.algorithms_process_mining.FootprintMatrix;
import projeto.api.dtos.NodeDurationAndMillisDTO;
import projeto.api.dtos.NodeDurationDTO;
import projeto.api.dtos.workflow_network.AbstractWorkflowNetworkStringNodeDTO;
import projeto.api.dtos.workflow_network.RelationMapDTO;
import projeto.controller.PartBean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter @Setter
public class ConformanceNetworkPerformanceDTO extends AbstractWorkflowNetworkStringNodeDTO< RelationMapDTO<NodeDurationDTO> >
{
    // NodeDurationMillis
    private List<NodeDurationAndMillisDTO> taskDurations = new ArrayList<>();
    private DeviationFlow deviations;
    private Set<String> moulds;
    private Set<String> parts;

    public ConformanceNetworkPerformanceDTO(FootprintMatrix footprint,
                                            List<NodeDurationDTO> taskDurations, List< RelationMapDTO<NodeDurationDTO> > transactionDurations,
                                            DeviationFlow deviations, Set<String> cases, boolean isSubProcess, PartBean partBean)
    {
        super( footprint );

        this.deviations = deviations;
        if(!isSubProcess){
            this.moulds = cases;
            this.parts = null;
        }else{
            this.parts = cases;

            Set<String> partsMoulds = new HashSet<>();
            for (String partCode : cases) {
                String mouldCode = partBean.getPartMouldCode(partCode);
                partsMoulds.add(mouldCode);
            }

            this.moulds = partsMoulds;
        }

        this.taskDurations = NodeDurationAndMillisDTO.convertFromDurationsDTO( taskDurations );
        this.relations = transactionDurations;

    }



}
