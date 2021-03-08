package projeto.api.dtos.workflow_network;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import projeto.algorithms_process_mining.FootprintMatrix;

import java.util.ArrayList;

@NoArgsConstructor
@Getter @Setter
public abstract class AbstractWorkflowNetworkStringNodeDTO< RMT extends RelationMapDTO<?> >
        extends AbstractWorkflowNetworkGenericNodeDTO<String, RMT>
{

    public AbstractWorkflowNetworkStringNodeDTO(FootprintMatrix footprint )
    {
        super( footprint );

        // Get different events
        this.nodes = new ArrayList<>( footprint.getEventNames() );

    }


}
