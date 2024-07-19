package org.natlex.geo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.natlex.geo.util.Status;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/11/24
 * Time: 11:09 PM
 */
@Data
public class GeologicalClassUpdateRequest {
    @NotBlank
    private String id;
    private String code;
    private String name;
    private Status status;
}
