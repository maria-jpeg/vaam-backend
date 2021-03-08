package projeto.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public abstract class NodeDurationAbstractDTO<T>
{
    private int node;
    private T duration;

}

