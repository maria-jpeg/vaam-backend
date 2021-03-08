package projeto.api.dtos;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class NodeDurationDTO extends NodeDurationAbstractDTO<DurationDTO>
{
    public NodeDurationDTO(int node, DurationDTO duration) { super(node, duration); }
}

