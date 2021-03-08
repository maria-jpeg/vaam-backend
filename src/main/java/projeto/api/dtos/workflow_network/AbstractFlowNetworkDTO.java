package projeto.api.dtos.workflow_network;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter @Setter
abstract public class AbstractFlowNetworkDTO< NT, RMT extends RelationMapDTO<?> >
{
    protected List<NT> nodes;
    protected List<RMT> relations;

    protected String info = "";

    public AbstractFlowNetworkDTO()
    {
        nodes = new ArrayList<>();
        relations = new ArrayList<>();
    }

    /*
    public AbstractFlowNetworkDTO(List<NT> nodes, List<RelationMapDTO<RT>> relations) {
        this.nodes = nodes;
        this.relations = relations;
    }

    */
}
