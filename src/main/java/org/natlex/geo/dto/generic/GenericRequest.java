package org.natlex.geo.dto.generic;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/11/24
 * Time: 11:07 PM
 */
public record GenericRequest<T>(
        @JsonFormat(shape = JsonFormat.Shape.STRING ,pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime timestamp,
        String version,
        @NotNull @Valid
        T data
) {}