
package projeto.controller.exceptions;


public class InvalidLogException extends Exception
{

    public InvalidLogException() {}

    public InvalidLogException(String message) { super(message); }

    public InvalidLogException(Throwable cause) {
        super(cause);
    }
}
