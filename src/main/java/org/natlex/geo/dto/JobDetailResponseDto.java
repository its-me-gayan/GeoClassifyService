package org.natlex.geo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.natlex.geo.util.JobStatus;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/12/24
 * Time: 10:18 PM
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobDetailResponseDto {
    private Long jobId;
    private String jobCompletedMessage;
    private JobStatus status;
}
