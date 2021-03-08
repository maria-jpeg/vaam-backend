package projeto.api.dtos.workflow_network;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public
class RelationMapDTO<T>
{
    int from;
    List<T> to;

}


