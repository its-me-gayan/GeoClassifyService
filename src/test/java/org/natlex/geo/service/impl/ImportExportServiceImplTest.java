package org.natlex.geo.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natlex.geo.dto.ExportJobResultDto;
import org.natlex.geo.dto.JobDetailResponseDto;
import org.natlex.geo.dto.generic.GenericResponse;
import org.natlex.geo.entity.ExportJob;
import org.natlex.geo.entity.ImportJob;
import org.natlex.geo.exception.FileExportFailedException;
import org.natlex.geo.exception.ValidationFailedException;
import org.natlex.geo.handler.FileHandler;
import org.natlex.geo.helper.EntityToDtoMapper;
import org.natlex.geo.helper.ResponseGenerator;
import org.natlex.geo.repository.ExportJobRepository;
import org.natlex.geo.repository.ImportJobRepository;
import org.natlex.geo.repository.SectionRepository;
import org.natlex.geo.service.ImportExportAsyncService;
import org.natlex.geo.util.JobStatus;
import org.springframework.http.HttpStatus;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/20/24
 * Time: 12:38 AM
 */
@ExtendWith(MockitoExtension.class)
class ImportExportServiceImplTest {

    @Mock
    private  ImportJobRepository importJobRepository;

    @Mock
    private  ExportJobRepository exportJobRepository;

    @Spy
    private  ImportExportAsyncService importExportAsyncService;

    @Spy
    private  ResponseGenerator responseGenerator;

    @Spy
    private  EntityToDtoMapper entityToDtoMapper;

    @InjectMocks
    private ImportExportServiceImpl importExportService;
   private ExportJob exportJob;
   private ImportJob importJob;
    @BeforeEach
    void setUp() {
         exportJob= ExportJob.builder()
                .id(1L)
                .completedAt(LocalDateTime.now())
                .jobCompletedMessage("File Export success and xlsx file saved")
                .status(JobStatus.IN_PROGRESS)
                .createdAt(LocalDateTime.now())
                .file(new byte[]{1, 2})
                .fileName("test")
                .build();

       importJob =  ImportJob.builder()
                .id(1L)
                .completedAt(LocalDateTime.now())
                .jobCompletedMessage("File Imported successfully")
                .status(JobStatus.IN_PROGRESS)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void test_importFile_Success() throws Exception {
        Mockito.when(importJobRepository.save(Mockito.any()))
                .thenReturn(importJob);

        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2});
        GenericResponse genericResponse = importExportService.importFile(inputStream, "xlxs");

        assertNotNull(genericResponse);
        assertEquals("Operation executed successfully", genericResponse.getResponseMessage());
        assertEquals("Import job created a submitted", genericResponse.getResponseDescription());
        assertEquals("000", genericResponse.getResponseCode());
        assertEquals(HttpStatus.ACCEPTED.value(), genericResponse.getHttpStatusCode());
        assertTrue(genericResponse.isSuccess());
        assertNotNull(genericResponse.getData());
        assertInstanceOf(JobDetailResponseDto.class, genericResponse.getData());
    }

    @Test
    void test_importFile_Failed_NullOrInvalidFileStream() throws Exception {
        ValidationFailedException ex = assertThrows(ValidationFailedException.class, () ->
                importExportService.importFile(null, "xlxs"));

        assertEquals("Import Failed - Invalid file or file input stream null",ex.getLocalizedMessage());
    }

    @Test
    void test_importFile_Failed_InvalidFileTypeExtension() throws Exception {
        ValidationFailedException ex = assertThrows(ValidationFailedException.class, () ->
                importExportService.importFile(new ByteArrayInputStream(new byte[]{1, 2}), "pdf"));

        assertEquals("File type extension is not supporting",ex.getLocalizedMessage());
    }

    @Test
    void test_initiateExportJob_Success() throws Exception {
        Mockito.when(exportJobRepository.save(Mockito.any()))
                .thenReturn(exportJob);

        GenericResponse genericResponse = importExportService.initiateExportJob();

        assertNotNull(genericResponse);
        assertEquals("Operation executed successfully",genericResponse.getResponseMessage());
        assertEquals("Export job created a submitted",genericResponse.getResponseDescription());
        assertEquals("000",genericResponse.getResponseCode());
        assertEquals(HttpStatus.ACCEPTED.value(),genericResponse.getHttpStatusCode());
        assertTrue(genericResponse.isSuccess());
        assertNotNull(genericResponse.getData());
        assertInstanceOf(JobDetailResponseDto.class, genericResponse.getData());
    }

    @Test
    void test_getImportJobStatusById_Success() throws Exception {
        Mockito.when(importJobRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(importJob));

        GenericResponse genericResponse = importExportService.
                getImportJobStatusById(1L);

        assertNotNull(genericResponse);
        assertEquals("Operation executed successfully",genericResponse.getResponseMessage());
        assertEquals("Import Job Found",genericResponse.getResponseDescription());
        assertEquals("000",genericResponse.getResponseCode());
        assertEquals(HttpStatus.OK.value(),genericResponse.getHttpStatusCode());
        assertTrue(genericResponse.isSuccess());
        assertNotNull(genericResponse.getData());
        assertInstanceOf(JobDetailResponseDto.class, genericResponse.getData());
    }

    @Test
    void test_getImportJobStatusById_Failed_InvalidJobId(){
        Mockito.when(importJobRepository.findById(Mockito.any()))
                .thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> importExportService.
                getImportJobStatusById(1L));

        assertEquals("Job not found - Invalid JobId",ex.getLocalizedMessage());
    }

    @Test
    void test_getExportedFileByJobId_Success() throws Exception {
exportJob.setStatus(JobStatus.DONE);
        Mockito.when(exportJobRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(exportJob));

        ExportJobResultDto exportJobResultDto = importExportService.
                getExportedFileByJobId(1L);

        assertNotNull(exportJobResultDto);
        assertNotNull(exportJobResultDto.getFile());
        assertNotNull(exportJobResultDto.getHeaderKey());
        assertNotNull(exportJobResultDto.getHeaderValue());


    }

    @Test
    void test_getExportedFileByJobId_Failed_InvalidJobId(){
        Mockito.when(exportJobRepository.findById(Mockito.any()))
                .thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> importExportService.
                getExportedFileByJobId(1L));

        assertEquals("Job not found - Invalid JobId",ex.getLocalizedMessage());
    }

    @Test
    void test_getExportedFileByJobId_Failed_JobNotCompletedYet(){
        Mockito.when(exportJobRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(exportJob));

        FileExportFailedException ex = assertThrows(FileExportFailedException.class, () -> importExportService.
                getExportedFileByJobId(1L));

        assertEquals("Cannot get imported file as this job "+exportJob.getStatus().name() +" and - "+exportJob.getJobCompletedMessage(),ex.getLocalizedMessage());
    }
    @Test
    void test_getExportJobByJobId_Success() throws Exception {
        exportJob.setStatus(JobStatus.DONE);
        Mockito.when(exportJobRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(exportJob));

        GenericResponse genericResponse = importExportService.
                getExportJobByJobId(1L);

        assertNotNull(genericResponse);
        assertEquals("Operation executed successfully",genericResponse.getResponseMessage());
        assertEquals("Export Job Found",genericResponse.getResponseDescription());
        assertEquals("000",genericResponse.getResponseCode());
        assertEquals(HttpStatus.OK.value(),genericResponse.getHttpStatusCode());
        assertTrue(genericResponse.isSuccess());
        assertNotNull(genericResponse.getData());
        assertInstanceOf(JobDetailResponseDto.class, genericResponse.getData());
    }

    @Test
    void test_getExportJobByJobId_Failed_InvalidJobId() throws Exception {
        Mockito.when(exportJobRepository.findById(Mockito.any()))
                .thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> importExportService.
                getExportJobByJobId(1L));

        assertEquals("Job not found - Invalid JobId",ex.getLocalizedMessage());
    }
}