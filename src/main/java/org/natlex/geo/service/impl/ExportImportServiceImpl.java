package org.natlex.geo.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.natlex.geo.dto.ExportJobResultDto;
import org.natlex.geo.dto.generic.GenericResponse;
import org.natlex.geo.entity.ExportJob;
import org.natlex.geo.entity.ImportJob;
import org.natlex.geo.exception.FileExportFailedException;
import org.natlex.geo.exception.ValidationFailedException;
import org.natlex.geo.helper.IEntityToDtoMapper;
import org.natlex.geo.helper.IResponseGenerator;
import org.natlex.geo.repository.IExportJobRepository;
import org.natlex.geo.repository.IImportJobRepository;
import org.natlex.geo.service.IExportImportAsyncService;
import org.natlex.geo.service.IExportImportService;
import org.natlex.geo.util.ExceptionMessages;
import org.natlex.geo.util.JobStatus;
import org.natlex.geo.util.ResponseMessages;
import org.natlex.geo.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/12/24
 * Time: 9:56 PM
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExportImportServiceImpl implements IExportImportService {

    private final IImportJobRepository importJobRepository;
    private final IExportJobRepository exportJobRepository;
    private final IExportImportAsyncService importExportAsyncService;
    private final IResponseGenerator responseGenerator;
    private final IEntityToDtoMapper entityToDtoMapper;
    @Override
    public GenericResponse importFile(InputStream inputStream,String originalName) throws Exception {

        if(!ValidationUtils.isSupportedExtension(originalName)){
            throw new ValidationFailedException(ExceptionMessages.VAL_IMPORT_FAILED_EXTENSION_NOT_SUP);
        }

        if(Objects.isNull(inputStream)){
            throw new ValidationFailedException(ExceptionMessages.VAL_IMPORT_FAILED_FILE_STREAM_NULL);
        }

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
