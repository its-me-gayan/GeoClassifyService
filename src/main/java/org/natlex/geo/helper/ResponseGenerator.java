package org.natlex.geo.helper;

import org.natlex.geo.dto.GeologicalClassResponseDto;
import org.natlex.geo.dto.SectionResponse;
import org.natlex.geo.dto.generic.GenericResponse;
import org.natlex.geo.util.ResponseCode;
import org.natlex.geo.util.ResponseMessages;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/12/24
 * Time: 12:07 AM
 */
@Component
public class ResponseGenerator {

    public GenericResponse generateSuccessResponse(Object data , String responseMessageDescription,HttpStatus status){
        return GenericResponse.builder()
                .timestamp(LocalDateTime.now())
                .responseCode(ResponseCode.SUCCESS)
                .responseMessage(ResponseMessages.OPERATION_EXECUTED)
                .responseDescription(responseMessageDescription)
                .data(data)
                .httpStatusCode(status.value())
                .isSuccess(Boolean.TRUE)
                .build();
    }

    public GenericResponse generateErrorExceptionResponse(String responseMessage , String responseMessageDescription,HttpStatus status) {
        return generateErrorResponse(responseMessage,responseMessageDescription,status, Collections.emptyList());
    }
    public GenericResponse generateErrorExceptionResponse(String responseMessage , String responseMessageDescription,HttpStatus status,List<String> fieldErrors) {
        return generateErrorResponse(responseMessage,responseMessageDescription,status, fieldErrors);
    }

    private GenericResponse generateErrorResponse(String responseMessage , String responseMessageDescription, HttpStatus status, List<String> fieldErrors){
        return GenericResponse.builder()
                .fieldError(fieldErrors)
                .responseDescription(responseMessageDescription)
                .responseMessage(responseMessage)
                .isSuccess(false)
                .responseCode(ResponseCode.COMMON_FAILED)
                .timestamp(LocalDateTime.now())
                .httpStatusCode(status.value())
                .build();
    }
}
