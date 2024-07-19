package org.natlex.geo.util;

import org.natlex.geo.dto.generic.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/14/24
 * Time: 10:35 PM
 */
public class ResponseUtil {

    private ResponseUtil() {
        // private constructor to prevent instantiation
    }
    public static ResponseEntity<GenericResponse> buildResponse(GenericResponse genericResponse) {
        return ResponseEntity
                .status(HttpStatus.valueOf(genericResponse.getHttpStatusCode()))
                .body(genericResponse);
    }
}
