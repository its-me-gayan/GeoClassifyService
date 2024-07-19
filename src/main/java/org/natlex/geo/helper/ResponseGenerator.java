package org.natlex.geo.helper;

import org.natlex.geo.dto.GeologicalClassResponseDto;
import org.natlex.geo.dto.SectionResponse;
import org.natlex.geo.dto.generic.GenericResponse;
import org.natlex.geo.util.ResponseMessages;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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
                .responseCode("000")
                .responseMessage(ResponseMessages.OPERATION_EXECUTED)
                .responseDescription(responseMessageDescription)
                .data(data)
                .httpStatusCode(status.value())
                .isSuccess(Boolean.TRUE)
                .build();
    }
}
