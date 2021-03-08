package projeto.api.dtos.workflow_network;


import java.util.List;

public class StatisticsWorkflowNetworkDTO<T> extends AbstractFlowNetworkDTO< T, RelationMapDTO<T> >
{

    public StatisticsWorkflowNetworkDTO()
    {
        super();
        this.info = "Statistics Network Model";
    }

    public StatisticsWorkflowNetworkDTO(String info)
    {
        super();
        this.info = info;
    }

    public StatisticsWorkflowNetworkDTO(List<T> nodes, List<RelationMapDTO<T>> relations)
    {
        super(nodes, relations, "Statistics Network Model");
    }

}
