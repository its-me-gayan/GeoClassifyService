package org.natlex.geo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.natlex.geo.util.Status;

import java.util.Set;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/11/24
 * Time: 11:21 PM
 */
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SectionResponse {
    private String id;
    private int sectionId;
    private String name;
    private Status status;
    private Set<GeologicalClassResponseDto> geologicalClass;
}
