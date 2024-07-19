package org.natlex.geo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.natlex.geo.util.Status;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/11/24
 * Time: 11:09 PM
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeologicalClassUpdateRequest {
    @NotBlank
    private String id;
    private String code;
    private String name;
    private Status status;
}
