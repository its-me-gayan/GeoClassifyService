package org.natlex.geo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/11/24
 * Time: 11:09 PM
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeologicalClassRequestDto {

    @NotBlank
    private String name;

    @NotBlank
    private String code;
}
