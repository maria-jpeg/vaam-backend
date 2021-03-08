
package projeto.controller.exceptions;


public class ParsingException extends Exception
{

    public ParsingException() {}

    public ParsingException(String message) { super(message); }

    public ParsingException(Throwable cause) {
        super(cause);
    }
}
