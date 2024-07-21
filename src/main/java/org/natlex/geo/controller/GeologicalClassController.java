package org.natlex.geo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.natlex.geo.dto.GeologicalClassRequestDto;
import org.natlex.geo.dto.GeologicalClassResponseDto;
import org.natlex.geo.dto.GeologicalClassUpdateRequest;
import org.natlex.geo.dto.SectionResponse;
import org.natlex.geo.dto.generic.GenericRequest;
import org.natlex.geo.dto.generic.GenericResponse;
import org.natlex.geo.service.GeologicalClassService;
import org.natlex.geo.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/12/24
 * Time: 1:55 PM
 *  * Controller class to handle CRUD operations for GeologicalClass entities.
 */

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Validated
@Log4j2
public class GeologicalClassController {

    private final GeologicalClassService geologicalClassService;

    /**
     * Endpoint to add a new geological class.
     * @param request Generic request containing GeologicalClassRequestDto
     * @return ResponseEntity with a generic response and status code
     * @throws Exception if there is an error during the addition process
     */

    @Operation(summary = "Add Geological classes API" ,description = "Add Geological classes API")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "Geological class successfully created", content =
                    { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = GeologicalClassResponseDto.class)) })
    )
    @PostMapping("/geo-log")
    public ResponseEntity<GenericResponse> addGeologicalClass(@Valid @RequestBody GenericRequest<GeologicalClassRequestDto> request) throws Exception {
        return ResponseUtil
                .buildResponse(geologicalClassService.addGeologicalClass(request.data()));
    }

    /**
     * Endpoint to retrieve all geological classes.
     * @return ResponseEntity with a generic response containing the list of geological classes and status code
     * @throws Exception if there is an error during the retrieval process
     */
    @Operation(summary = "GetAll Geological classes API" ,description = "GetAll available Geological classes API")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "Geological class found", content =
                    { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = GeologicalClassResponseDto.class)) })
    )
    @GetMapping("/geo-log")
    public ResponseEntity<GenericResponse> getAllGeologicalClass() throws Exception {
        return ResponseUtil
                .buildResponse(geologicalClassService.getAllGeologicalClass());
    }

    /**
     * Endpoint to update an existing geological class.
     * @param genericRequest Generic request containing GeologicalClassUpdateRequest
     * @return ResponseEntity with a generic response and status code
     * @throws Exception if there is an error during the update process
     */
    @Operation(summary = "Update Geological classes API" ,description = "Update available Geological classes API")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "Geological updated success", content =
                    { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = GeologicalClassResponseDto.class)) })
    )
    @PatchMapping("/geo-log")
    public ResponseEntity<GenericResponse> updateGeologicalClass(@Valid @RequestBody GenericRequest<GeologicalClassUpdateRequest> genericRequest) throws Exception {
        return ResponseUtil
                .buildResponse(geologicalClassService.updateGeologicalClass(genericRequest.data()));
    }

    /**
     * Endpoint to delete a geological class by its code.
     * @param code The code of the geological class to be deleted
     * @return ResponseEntity with a generic response and status code
     * @throws Exception if there is an error during the deletion process
     */
    @Operation(summary = "Delete Geological classes API" ,description = "Delete available Geological classes API")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "Geological Deletion success", content =
                    { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = GeologicalClassResponseDto.class)) })
    )
    @DeleteMapping("/geo-log/{code}")
    public ResponseEntity<GenericResponse> deleteGeologicalClass(@Valid @PathVariable(value = "code") @NotBlank String code) throws Exception {
        return ResponseUtil
                .buildResponse(geologicalClassService.deleteGeologicalClass(code));
    }

    /**
     * Endpoint to find a geological class by its code.
     * @param code The code of the geological class to be retrieved
     * @return ResponseEntity with a generic response and status code
     * @throws Exception if there is an error during the retrieval process
     */
    @Operation(summary = "Find Geological class from code" ,description = "Find Geological class from code")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "Geological found", content =
                    { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = GeologicalClassResponseDto.class)) })
    )
    @GetMapping("/geo-log/by-code")
    public ResponseEntity<GenericResponse> getGeologicalClassByCode(@Valid @RequestParam("code") @NotBlank String code) throws Exception {
        return ResponseUtil
                .buildResponse( geologicalClassService.getGeologicalClassByCode(code));
    }
}
