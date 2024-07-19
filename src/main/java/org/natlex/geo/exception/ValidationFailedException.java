package org.natlex.geo.exception;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/13/24
 * Time: 10:17 PM
 */
public class ValidationFailedException extends RuntimeException{

    public ValidationFailedException() {
        super();
    }

    public ValidationFailedException(String message) {
        super(message);
    }
}
