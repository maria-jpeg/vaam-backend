
package projeto.controller.exceptions;


public class MyException extends Exception
{

    public MyException() {}

    public MyException(String message) { super(message); }

    public MyException(Throwable cause) {
        super(cause);
    }
}
