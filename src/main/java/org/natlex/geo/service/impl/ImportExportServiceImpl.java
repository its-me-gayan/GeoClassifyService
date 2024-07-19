package org.natlex.geo.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.natlex.geo.dto.ExportJobResultDto;
import org.natlex.geo.dto.JobDetailResponseDto;
import org.natlex.geo.dto.generic.GenericResponse;
import org.natlex.geo.entity.ExportJob;
import org.natlex.geo.entity.ImportJob;
import org.natlex.geo.exception.FileExportFailedException;
import org.natlex.geo.helper.EntityToDtoMapper;
import org.natlex.geo.helper.ResponseGenerator;
import org.natlex.geo.repository.ExportJobRepository;
import org.natlex.geo.repository.ImportJobRepository;
import org.natlex.geo.service.ImportExportAsyncService;
import org.natlex.geo.service.ImportExportService;
import org.natlex.geo.util.ExceptionMessages;
import org.natlex.geo.util.JobStatus;
import org.natlex.geo.util.ResponseMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDateTime;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/12/24
 * Time: 9:56 PM
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImportExportServiceImpl implements ImportExportService {

    private final ImportJobRepository importJobRepository;
    private final ExportJobRepository exportJobRepository;
    private final ImportExportAsyncService importExportAsyncService;
    private final ResponseGenerator responseGenerator;
    private final EntityToDtoMapper entityToDtoMapper;
    @Override
    public GenericResponse importFile(InputStream inputStream) throws Exception {
        ImportJob importJob = ImportJob.builder()
                .status(JobStatus.IN_PROGRESS)
                .createdAt(LocalDateTime.now())
                .build();
        ImportJob createdImportJob = importJobRepository.save(importJob);

        importExportAsyncService.createAndSubmitFileImportAsyncJob(inputStream,createdImportJob.getId());


        return responseGenerator.generateSuccessResponse(
                entityToDtoMapper.mapImportJobEntityToDto(importJob),
                ResponseMessages.IMP_JOB_SUBMITTED,
                HttpStatus.ACCEPTED);
    }

    @Override
    public GenericResponse initiateExportJob() throws Exception {
        ExportJob exportJob = ExportJob.builder()
                .status(JobStatus.IN_PROGRESS)
                .createdAt(LocalDateTime.now())
                .build();
        ExportJob createdExportJob = exportJobRepository.save(exportJob);

        importExportAsyncService.createAndSubmitFileExportAsyncJob(createdExportJob.getId());

        return responseGenerator.generateSuccessResponse(
                entityToDtoMapper.mapExportJobEntityToDto(createdExportJob),
                ResponseMessages.EXP_JOB_SUBMITTED,
                HttpStatus.ACCEPTED);
    }

    @Override
    public GenericResponse getImportJobStatusById(Long jobId) throws Exception {
        ImportJob importJob = importJobRepository
                .findById(jobId).orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.JOB_NOT_FOUND));

        return responseGenerator.generateSuccessResponse(
                entityToDtoMapper.mapImportJobEntityToDto(importJob),
                ResponseMessages.IMP_JOB_FOUND,
                HttpStatus.OK);
    }

    @Override
    public ExportJobResultDto getExportedFileByJobId(Long jobId) throws Exception {

        ExportJob exportJob = exportJobRepository
                .findById(jobId).orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.JOB_NOT_FOUND));

        if(exportJob.getStatus().equals(JobStatus.DONE)){
            return ExportJobResultDto.builder()
                    .file(exportJob.getFile())
                    .headerKey("Content-Disposition")
                    .headerValue("attachment;filename="+exportJob.getFileName())
                    .build();
        }else {

            throw new FileExportFailedException(
                    "Cannot get imported file as this job "+exportJob.getStatus().name() +" and - "+exportJob.getJobCompletedMessage()
            );
        }

    }

    @Override
    public GenericResponse getExportJobByJobId(Long jobId) throws Exception {
        ExportJob exportJob = exportJobRepository
                .findById(jobId).orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.JOB_NOT_FOUND));

        return responseGenerator.generateSuccessResponse(
                entityToDtoMapper.mapExportJobEntityToDto(exportJob),
                ResponseMessages.EXP_JOB_FOUND,
                HttpStatus.OK);
    }
}
