package org.natlex.geo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.natlex.geo.dto.SectionRequest;
import org.natlex.geo.dto.SectionResponse;
import org.natlex.geo.dto.SectionUpdateRequest;
import org.natlex.geo.dto.generic.GenericRequest;
import org.natlex.geo.dto.generic.GenericResponse;
import org.natlex.geo.service.SectionService;
import org.natlex.geo.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/11/24
 * Time: 10:48 PM
 * Controller class to handle CRUD operations for Section entities.
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Validated
public class SectionController {

    private final SectionService sectionService;

    /**
     * Endpoint to add a new section with or without geological classes.
     * @param addSectionRequest Generic request containing SectionRequest
     * @return ResponseEntity with a generic response and status code
     * @throws Exception if there is an error during the addition process
     */
    @Operation(summary = "Add Section API" ,description = "Add sections with or without geologic classes")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "Section successfully created", content =
                    { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = SectionResponse.class)) })
    )
    @PostMapping("/section")
    public ResponseEntity<GenericResponse> addSection(@RequestBody @Valid GenericRequest<SectionRequest> addSectionRequest) throws Exception {
        return ResponseUtil
                .buildResponse(sectionService.addSection(addSectionRequest.data()));
    }

    /**
     * Endpoint to retrieve all sections with their geological classes.
     * @return ResponseEntity with a generic response containing the list of sections and status code
     * @throws Exception if there is an error during the retrieval process
     */
    @Operation(summary = "Get All Sections API" ,description = "Get All sections with its geological classes without pagination")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "Sections found and attached", content =
                    { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = SectionResponse.class)) })
    )
    @GetMapping("/section")
    public ResponseEntity<GenericResponse> getAllSections() throws Exception {
        return ResponseUtil
                .buildResponse(sectionService.getAllSections());
    }

    /**
     * Endpoint to retrieve sections by geological class code.
     * @param code The code of the geological class to filter sections
     * @return ResponseEntity with a generic response and status code
     * @throws Exception if there is an error during the retrieval process
     */
    @Operation(summary = "Get relevant section by bound geological class code" ,description = "Get relevant section by bound geological class code")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "Section found and attached", content =
                    { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = SectionResponse.class)) })
    )
    @GetMapping("/section/by-code")
    public ResponseEntity<GenericResponse> getSectionsByGeoClassCode(@Valid @RequestParam("code") @NotBlank String code) throws Exception {
        return ResponseUtil
                .buildResponse(sectionService.getAllSectionsByGeoLocCode(code));
    }

    /**
     * Endpoint to update an existing section with or without geological classes.
     * @param sectionUpdateRequest Generic request containing SectionUpdateRequest
     * @return ResponseEntity with a generic response and status code
     * @throws Exception if there is an error during the update process
     */
    @Operation(summary = "Update section API" ,description = "Update section with or without geological class")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "Section update success", content =
                    { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = SectionResponse.class)) })
    )
    @PatchMapping("/section")
    public ResponseEntity<GenericResponse> updateSection(@Valid @RequestBody GenericRequest<SectionUpdateRequest> sectionUpdateRequest) throws Exception {
        return ResponseUtil
                .buildResponse(sectionService.updateSection(sectionUpdateRequest.data()));
    }


    /**
     * Endpoint to delete a section by its UUID.
     * @param sectionId The UUID of the section to be deleted
     * @return ResponseEntity with a generic response and status code
     * @throws Exception if there is an error during the deletion process
     */
    @Operation(summary = "Delete section API" ,description = "Delete section by section UUID")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "Section delete success", content =
                    { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = SectionResponse.class)) })
    )
    @DeleteMapping("/section/{id}")
    public ResponseEntity<GenericResponse> deleteSection(@Valid @PathVariable("id") @NotBlank String sectionId) throws Exception {
        return ResponseUtil
                .buildResponse(sectionService.deleteSection(sectionId));
    }
}
