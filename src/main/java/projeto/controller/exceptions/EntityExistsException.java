/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projeto.controller.exceptions;

/**
 *
 * @author poke
 */
public class EntityExistsException extends Exception
{

    public EntityExistsException() {}

    public EntityExistsException(String message) { super(message); }

    /*
    public EntityExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityExistsException(Throwable cause) {
        super(cause);
    }

    public EntityExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    */
    
}
