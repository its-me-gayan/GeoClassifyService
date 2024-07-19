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
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Validated
public class SectionController {

    private final SectionService sectionService;


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
