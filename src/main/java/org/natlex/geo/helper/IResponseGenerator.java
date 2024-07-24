package org.natlex.geo.helper;

import org.natlex.geo.dto.generic.GenericResponse;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/24/24
 * Time: 10:15 PM
 */
public interface IResponseGenerator {
    GenericResponse generateSuccessResponse(Object data, String responseMessageDescription, HttpStatus status);
    GenericResponse generateErrorResponse(String responseMessage, String responseMessageDescription, HttpStatus status);
    GenericResponse generateErrorResponse(String responseMessage, String responseMessageDescription, HttpStatus status, List<String> fieldErrors);
}
