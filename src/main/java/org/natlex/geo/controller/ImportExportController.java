package org.natlex.geo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.natlex.geo.dto.ExportJobResultDto;
import org.natlex.geo.dto.JobDetailResponseDto;
import org.natlex.geo.dto.generic.GenericResponse;
import org.natlex.geo.service.IExportImportService;
import org.natlex.geo.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/12/24
 * Time: 6:08 PM
 * Controller class to handle import and export operations for sections.
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImportExportController {
private final IExportImportService importExportService;


    /**
     * Endpoint to import sections from an Excel file.
     * @param file MultipartFile containing the Excel file to be imported
     * @return ResponseEntity with a generic response and status code
     * @throws Exception if there is an error during the import process
     */
    @Operation(summary = "Import section from a excel file" ,description = "Import sections from a excel file")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "Section import started", content =
                    { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = JobDetailResponseDto.class)) })
    )
    @PostMapping("/import")
    public ResponseEntity<GenericResponse> importSections(@RequestParam("file") MultipartFile file) throws Exception {
        return ResponseUtil
                .buildResponse(importExportService.importFile(file.getInputStream(),file.getOriginalFilename()));
    }

    /**
     * Endpoint to get the status of an import job by its ID.
     * @param id The ID of the import job
     * @return ResponseEntity with a generic response containing the job details and status code
     * @throws Exception if there is an error during the retrieval process
     */
    @Operation(summary = "Get Import job details by passing job id" ,description = "Get Import job details by passing job id")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "Job found", content =
                    { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = JobDetailResponseDto.class)) })
    )
    @GetMapping("/import/{id}")
    public ResponseEntity<GenericResponse> getImportJobStatus(@PathVariable Long id) throws Exception {
        return ResponseUtil
                .buildResponse(importExportService.getImportJobStatusById(id));
    }

    /**
     * Endpoint to export all sections and geological classes to an Excel file.
     * @return ResponseEntity with a generic response containing the job details and status code
     * @throws Exception if there is an error during the export process
     */
    @Operation(summary = "Export all data to excel file" ,description = "Export all available sections and geological classes to excel file")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "Export started", content =
                    { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = JobDetailResponseDto.class)) })
    )
    @GetMapping("/export")
    public ResponseEntity<GenericResponse> exportSections() throws Exception {
        return ResponseUtil
                .buildResponse(importExportService.initiateExportJob());
    }

    /**
     * Endpoint to get the status of an export job by its ID.
     * @param id The ID of the export job
     * @return ResponseEntity with a generic response containing the job details and status code
     * @throws Exception if there is an error during the retrieval process
     */
    @Operation(summary = "Get export job details by id" ,description = "Get export job details by id")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "Job Found", content =
                    { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = JobDetailResponseDto.class)) })
    )
    @GetMapping("/export/{id}")
    public ResponseEntity<GenericResponse> getExportJobStatus(@PathVariable Long id) throws Exception {
        return ResponseUtil
                .buildResponse( importExportService.getExportJobByJobId(id));
    }

    /**
     * Endpoint to download the exported file by job ID.
     * @param id The ID of the export job
     * @return ResponseEntity containing the exported file as a byte array and status code
     * @throws Exception if there is an error during the file retrieval process
     */
    @Operation(summary = "Download exported file by job id" ,description = "Download exported file by job id")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "file downloaded", content =
                    { @Content(mediaType = "application/octet-stream", schema =
                    @Schema(implementation = byte[].class)) })
    )
    @GetMapping("/export/{id}/file")
    public ResponseEntity<byte[]> getExportedFile(@PathVariable Long id) throws Exception {
        ExportJobResultDto exportedFileByJobId = importExportService.getExportedFileByJobId(id);

        return ResponseEntity.status(HttpStatus.OK)
                .header(exportedFileByJobId.getHeaderKey(),exportedFileByJobId.getHeaderValue())
               .contentLength(exportedFileByJobId.getFile().length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
               .body(exportedFileByJobId.getFile());
    }

}
