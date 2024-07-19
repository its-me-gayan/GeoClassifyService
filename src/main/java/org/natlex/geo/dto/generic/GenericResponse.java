package org.natlex.geo.dto.generic;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/11/24
 * Time: 11:18 PM
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GenericResponse {

    private LocalDateTime timestamp;
    private String responseMessage;
    private String responseDescription;
    private String responseCode;
    private Integer httpStatusCode;
    private boolean isSuccess;
    List<String> fieldError;
    private Object data;
}
