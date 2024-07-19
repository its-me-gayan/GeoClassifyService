package org.natlex.geo.exception;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/12/24
 * Time: 2:51 PM
 */
public class EntityExistException extends RuntimeException{
    public EntityExistException(String message) {
        super(message);
    }
}
