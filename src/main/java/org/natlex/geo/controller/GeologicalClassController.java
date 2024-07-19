package org.natlex.geo.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.natlex.geo.dto.GeologicalClassRequestDto;
import org.natlex.geo.dto.GeologicalClassUpdateRequest;
import org.natlex.geo.dto.generic.GenericRequest;
import org.natlex.geo.dto.generic.GenericResponse;
import org.natlex.geo.service.GeologicalClassService;
import org.natlex.geo.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/12/24
 * Time: 1:55 PM
 */

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Validated
@Log4j2
public class GeologicalClassController {

    private final GeologicalClassService geologicalClassService;
    @PostMapping("/geo-log")
    public ResponseEntity<GenericResponse> addGeologicalClass(@Valid @RequestBody GenericRequest<GeologicalClassRequestDto> request) throws Exception {
        return ResponseUtil
                .buildResponse(geologicalClassService.addGeologicalClass(request.data()));
    }

    @GetMapping("/geo-log")
    public ResponseEntity<GenericResponse> getAllGeologicalClass() throws Exception {
        return ResponseUtil
                .buildResponse(geologicalClassService.getAllGeologicalClass());
    }

    @PatchMapping("/geo-log")
    public ResponseEntity<GenericResponse> updateGeologicalClass(@Valid @RequestBody GenericRequest<GeologicalClassUpdateRequest> genericRequest) throws Exception {
        return ResponseUtil
                .buildResponse(geologicalClassService.updateGeologicalClass(genericRequest.data()));
    }

    @DeleteMapping("/geo-log/{code}")
    public ResponseEntity<GenericResponse> deleteGeologicalClass(@Valid @PathVariable(value = "code") @NotBlank String code) throws Exception {
        return ResponseUtil
                .buildResponse(geologicalClassService.deleteGeologicalClass(code));
    }

    @GetMapping("/geo-log/by-code")
    public ResponseEntity<GenericResponse> getGeologicalClassByCode(@Valid @RequestParam("code") @NotBlank String code) throws Exception {
        return ResponseUtil
                .buildResponse( geologicalClassService.getGeologicalClassByCode(code));
    }
}
