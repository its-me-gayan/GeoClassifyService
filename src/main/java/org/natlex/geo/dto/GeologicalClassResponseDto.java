package org.natlex.geo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.natlex.geo.util.Status;

import java.time.LocalDateTime;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/11/24
 * Time: 11:09 PM
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GeologicalClassResponseDto {
    private String id;
    private String name;
    private String code;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
