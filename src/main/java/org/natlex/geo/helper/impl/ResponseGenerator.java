package org.natlex.geo.helper.impl;

import org.natlex.geo.dto.GeologicalClassResponseDto;
import org.natlex.geo.dto.SectionResponse;
import org.natlex.geo.dto.generic.GenericResponse;
import org.natlex.geo.helper.IResponseGenerator;
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
public class ResponseGenerator implements IResponseGenerator {
    public GenericResponse generateSuccessResponse(Object data, String responseMessageDescription, HttpStatus status) {
        return buildResponse(
                ResponseCode.SUCCESS,
                ResponseMessages.OPERATION_EXECUTED,
                responseMessageDescription,
                data,
                status,
                true,
                Collections.emptyList()
        );
    }

    public GenericResponse generateErrorResponse(String responseMessage, String responseMessageDescription, HttpStatus status) {
        return generateErrorResponse(responseMessage, responseMessageDescription, status, Collections.emptyList());
    }

    public GenericResponse generateErrorResponse(String responseMessage, String responseMessageDescription, HttpStatus status, List<String> fieldErrors) {
        return buildResponse(
                ResponseCode.COMMON_FAILED,
                responseMessage,
                responseMessageDescription,
                null,
                status,
                false,
                fieldErrors
        );
    }

    private GenericResponse buildResponse(String responseCode, String responseMessage, String responseDescription, Object data, HttpStatus status, boolean isSuccess, List<String> fieldErrors) {
        return GenericResponse.builder()
                .timestamp(LocalDateTime.now())
                .responseCode(responseCode)
                .responseMessage(responseMessage)
                .responseDescription(responseDescription)
                .data(data)
                .httpStatusCode(status.value())
                .isSuccess(isSuccess)
                .fieldError(fieldErrors)
                .build();
    }
}
