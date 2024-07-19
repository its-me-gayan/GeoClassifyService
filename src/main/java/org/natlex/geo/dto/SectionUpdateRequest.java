package org.natlex.geo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.natlex.geo.util.Status;

import java.util.Set;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/11/24
 * Time: 11:09 PM
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SectionUpdateRequest {
    @NotBlank
    private String id;
    private Status status;
    private Set<GeologicalClassRequestDto> geologicalClass;
}
