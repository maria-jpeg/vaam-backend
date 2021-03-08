package projeto.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class Relation
{
    private int from;
    private int to;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Relation relation = (Relation) o;
        return from == relation.from &&
                to == relation.to;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }
}
