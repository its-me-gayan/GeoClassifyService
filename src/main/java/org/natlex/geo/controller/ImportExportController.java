package org.natlex.geo.controller;

import lombok.RequiredArgsConstructor;
import org.natlex.geo.dto.ExportJobResultDto;
import org.natlex.geo.dto.generic.GenericResponse;
import org.natlex.geo.service.ImportExportService;
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
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImportExportController {
private final ImportExportService importExportService;
    @PostMapping("/import")
    public ResponseEntity<GenericResponse> importSections(@RequestParam("file") MultipartFile file) throws Exception {
        return ResponseUtil
                .buildResponse(importExportService.importFile(file.getInputStream()));
    }

    @GetMapping("/import/{id}")
    public ResponseEntity<GenericResponse> getImportJobStatus(@PathVariable Long id) throws Exception {
        return ResponseUtil
                .buildResponse(importExportService.getImportJobStatusById(id));
    }

    @GetMapping("/export")
    public ResponseEntity<GenericResponse> exportSections() throws Exception {
        return ResponseUtil
                .buildResponse(importExportService.initiateExportJob());
    }

    @GetMapping("/export/{id}")
    public ResponseEntity<GenericResponse> getExportJobStatus(@PathVariable Long id) throws Exception {
        return ResponseUtil
                .buildResponse( importExportService.getExportJobByJobId(id));
    }

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
