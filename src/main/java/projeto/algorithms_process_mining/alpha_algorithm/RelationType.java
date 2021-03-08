package projeto.algorithms_process_mining.alpha_algorithm;

public enum RelationType {
    // ->
    PRECEDES('>'),
    // <-
    FOLLOWS('<'),
    // ||
    PARALLEL('|'),
    // #
    NOT_CONNECTED('#');

    RelationType(char symbol) {
        this.sym = symbol;
    }

    private final char sym;

    public char symbol() {
        return sym;
    }

    @Override
    public String toString() {
        return String.valueOf( sym );
    }
}
