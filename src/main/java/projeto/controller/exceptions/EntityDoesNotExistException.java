/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projeto.controller.exceptions;

import java.io.Serializable;

/**
 *
 * @author poke
 */
public class EntityDoesNotExistException extends Exception implements Serializable
{

    public EntityDoesNotExistException() {}

    public EntityDoesNotExistException(String message) { super(message); }

    /*
    public EntityDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityDoesNotExistException(Throwable cause) {
        super(cause);
    }

    public EntityDoesNotExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    */
}
