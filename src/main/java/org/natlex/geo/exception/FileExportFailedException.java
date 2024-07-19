package org.natlex.geo.exception;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/15/24
 * Time: 5:42 PM
 */
public class FileExportFailedException extends RuntimeException{


    public FileExportFailedException() {
        super();
    }

    public FileExportFailedException(String message) {
        super(message);
    }
}
