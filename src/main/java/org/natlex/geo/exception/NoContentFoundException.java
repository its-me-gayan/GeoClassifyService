package org.natlex.geo.exception;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/13/24
 * Time: 6:24 PM
 */
public class NoContentFoundException extends RuntimeException{
    public NoContentFoundException() {
        super();
    }

    public NoContentFoundException(String message) {
        super(message);
    }
}
