package projeto.api.dtos.conformance.performance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import projeto.core.Relation;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter @Setter
public class DeviationFlow
{
    List<Integer> nodes;
    List<Relation> relations;


    public DeviationFlow()
    {
        this.nodes = new ArrayList<>();
        this.relations = new ArrayList<>();
    }
}
