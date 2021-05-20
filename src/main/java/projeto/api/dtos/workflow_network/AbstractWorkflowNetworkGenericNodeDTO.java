package projeto.api.dtos.workflow_network;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import projeto.algorithms_process_mining.FootprintMatrix;
import projeto.algorithms_process_mining.alpha_miner_prom.AlphaMinerProm;
import projeto.algorithms_process_mining.inductive_miner.InductiveMiner;
import projeto.api.dtos.NodeFrequencyDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Getter @Setter
public abstract class AbstractWorkflowNetworkGenericNodeDTO< NT, RMT extends RelationMapDTO<?> >
        extends AbstractFlowNetworkDTO<NT, RMT>
{
    protected List<NodeFrequencyDTO> startEvents;
    protected List<NodeFrequencyDTO> endEvents;

    public AbstractWorkflowNetworkGenericNodeDTO( FootprintMatrix footprint )
    {

        // Get Start and End events
        //If it is inductive miner do something else
        if (footprint.getAlgorithm().getClass() == InductiveMiner.class || footprint.getAlgorithm().getClass() == AlphaMinerProm.class){
            this.startEvents = buildStartFrequencies( footprint );
            this.endEvents = buildEndFrequencies( footprint );
        }else{ //Isto era o que estava no pmviz. Nem sei como funciona :)
            this.startEvents = buildStartFrequencies( footprint );
            this.endEvents = buildStartEndFrequencies( footprint );
        }

        // Information about algorithm used to derive the model
        this.info = footprint.getInfo();

    }

    protected List<NodeFrequencyDTO> buildStartFrequencies ( FootprintMatrix footprint )
    {
        HashMap<Integer, Integer> events = footprint.getStartEvents();
        List<NodeFrequencyDTO> nodeFrequencies = new ArrayList<>();

        for( Map.Entry<Integer, Integer> event : events.entrySet() )
        {
            nodeFrequencies.add(
                    new NodeFrequencyDTO( event.getKey(), event.getValue() )
            );

        }
        return nodeFrequencies;

    }

    protected List<NodeFrequencyDTO> buildStartEndFrequencies ( FootprintMatrix footprint )
    {
        HashMap<Integer, Integer> events = footprint.getEndEvents();
        List<NodeFrequencyDTO> nodeFrequencies = new ArrayList<>();
        int numberOfEvents = footprint.getEventNamesMapper().size();

        for( Map.Entry<Integer, Integer> event : events.entrySet() )
        {
            Integer eventID = event.getKey();

            boolean isFinalNode = true;

            for ( int i = 0; i < numberOfEvents; i++ )
            {
                if( footprint.areEventsConnected( eventID, i ) )
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

    //For im
    protected List<NodeFrequencyDTO> buildEndFrequencies ( FootprintMatrix footprint )
    {
        HashMap<Integer, Integer> events = footprint.getEndEvents();
        List<NodeFrequencyDTO> nodeFrequencies = new ArrayList<>();

        for( Map.Entry<Integer, Integer> event : events.entrySet() )
        {
            nodeFrequencies.add(
                    new NodeFrequencyDTO( event.getKey(), event.getValue() )
            );

        }
        return nodeFrequencies;
    }
}
