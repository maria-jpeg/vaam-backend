package projeto.api.dtos.workflow_network;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import projeto.algorithms_process_mining.FootprintMatrix;

@NoArgsConstructor
@Getter @Setter
public class WorkflowNetworkDTO extends AbstractWorkflowNetworkStringNodeDTO< RelationMapDTO<Integer> >
{
    private StatisticsWorkflowNetworkDTO<NodeStats> statistics;

    public WorkflowNetworkDTO( FootprintMatrix footprint )
    {
        super( footprint );

        // Calculate statistics and fill relations
        // To avoid doing the for cycle 2 times
        this.statistics = footprint.getFootprintStatistics().getStatisticsNetwork( this.relations );

    }


    /*
    @Deprecated
    public WorkflowNetworkDTO(Set<String> nodes, RelationType[][] footprint, StatisticsFlowNetworkDTO<NodeStats> statisticsNetwork)
    {
        //footprintStatistics.getFrequency()[i][j]
        this.nodes = new ArrayList<>( nodes );

        List<Integer> nodeRelationsList = null;
        for (int i = 0; i < footprint.length; i++)
        {
            nodeRelationsList = new ArrayList<>();
            RelationType[] relation = footprint[i];
            for (int j = 0; j < relation.length; j++)
            {
                if( relation[j] == RelationType.PRECEDES || relation[j] == RelationType.PARALLEL )
                {
                    nodeRelationsList.add( j );
                }
            }
            if( !nodeRelationsList.isEmpty() )
                this.relations.add( new RelationMapDTO<>( i, nodeRelationsList ) );
        }

        this.statistics = statisticsNetwork;
    }

    */

}
