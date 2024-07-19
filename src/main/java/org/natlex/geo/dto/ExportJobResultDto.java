package org.natlex.geo.dto;

import lombok.Builder;
import lombok.Data;
import org.natlex.geo.util.Status;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/15/24
 * Time: 5:33 PM
 */
@Builder
@Data
public class ExportJobResultDto {
    private byte[] file;
    private String headerKey;
    private String headerValue;
}
