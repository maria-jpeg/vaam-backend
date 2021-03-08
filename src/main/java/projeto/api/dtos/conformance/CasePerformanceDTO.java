package projeto.api.dtos.conformance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import projeto.algorithms_process_mining.FootprintMatrix;
import projeto.api.dtos.NodeDurationDTO;
import projeto.api.dtos.NodeFrequencyDTO;
import projeto.api.dtos.workflow_network.AbstractWorkflowNetworkGenericNodeDTO;
import projeto.api.dtos.workflow_network.RelationMapDTO;
import projeto.controller.PartBean;

import java.util.*;

@AllArgsConstructor
@Getter @Setter
public class CasePerformanceDTO extends AbstractWorkflowNetworkGenericNodeDTO< NodeDurationDTO, RelationMapDTO<NodeDurationDTO> >
{
    // Cases found with filters
    //private Set<Long> cases;
    // Moulds found with filters
    private Set<String> moulds;
    // Parts found with filters
    private Set<String> parts;

    /*public CasePerformanceDTO(FootprintMatrix footprint,
                              List<NodeDurationDTO> taskDurations, List< RelationMapDTO<NodeDurationDTO> > transactionDurations,
                              Set<Long> cases )
    {
        super(footprint);
        this.nodes = taskDurations;
        this.relations = transactionDurations;
        this.cases = cases;
    }*/

    public CasePerformanceDTO(FootprintMatrix footprint,
                              List<NodeDurationDTO> taskDurations, List< RelationMapDTO<NodeDurationDTO> > transactionDurations,
                              Set<String> cases, boolean isSubProcess, PartBean partBean)
    {
        super(footprint);
        this.nodes = taskDurations;
        this.relations = transactionDurations;
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
    }

    // override
    // to use frequencies in stead of the model
    @Override
    protected List<NodeFrequencyDTO> buildStartEndFrequencies (FootprintMatrix footprint )
    {
        HashMap<Integer, Integer> events = footprint.getEndEvents();
        List<NodeFrequencyDTO> nodeFrequencies = new ArrayList<>();
        int[][] frequencies = footprint.getFootprintStatistics().getFrequency();

        int numberOfEvents = footprint.getEventNamesMapper().size();

        for( Map.Entry<Integer, Integer> event : events.entrySet() )
        {
            Integer eventID = event.getKey();

            boolean isFinalNode = true;

            for ( int i = 0; i < numberOfEvents; i++ )
            {
                if( frequencies[eventID][i] > 0 )
                {
                    isFinalNode = false;
                    break;
                }

            }

            if( ! isFinalNode )
                continue;

            nodeFrequencies.add(
                    new NodeFrequencyDTO( eventID, event.getValue() )
            );

        }
        return nodeFrequencies;

    }

}
