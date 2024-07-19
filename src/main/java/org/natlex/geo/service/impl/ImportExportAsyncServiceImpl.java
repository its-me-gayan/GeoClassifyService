package org.natlex.geo.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.natlex.geo.entity.ExportJob;
import org.natlex.geo.entity.GeologicalClass;
import org.natlex.geo.entity.ImportJob;
import org.natlex.geo.entity.Section;
import org.natlex.geo.exception.ValidationFailedException;
import org.natlex.geo.handler.FileHandler;
import org.natlex.geo.helper.DtoToEntityMapper;
import org.natlex.geo.helper.EntityToDtoMapper;
import org.natlex.geo.repository.ExportJobRepository;
import org.natlex.geo.repository.GeoLogicalClassRepository;
import org.natlex.geo.repository.ImportJobRepository;
import org.natlex.geo.repository.SectionRepository;
import org.natlex.geo.service.ImportExportAsyncService;
import org.natlex.geo.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/12/24
 * Time: 6:22 PM
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class ImportExportAsyncServiceImpl implements ImportExportAsyncService {
    private final ImportJobRepository importJobRepository;
    private final ExportJobRepository exportJobRepository;
    private final SectionRepository sectionRepository;
    private final FileHandler fileHandler;


    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    @Async
    @Override
    public void createAndSubmitFileImportAsyncJob(InputStream inputStream, Long jobId) {
        log.info("File import job scheduled and running");
        ImportJob importJob = importJobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.IMPORT_FAILED_INVALID_JOB_ID));
        try {
            List<Section> sections = fileHandler.importFile(inputStream);
            if(CollectionUtils.isEmpty(sections)){
                throw new ValidationFailedException(ExceptionMessages.VAL_IMPORT_FAILED_NO_SECTIONS);
            }

            sectionRepository.saveAll(sections);
            importJob.setStatus(JobStatus.DONE);
            importJob.setJobCompletedMessage(ResponseMessages.FILE_IMPORT_SUCCESS);
        } catch (Exception e) {
            importJob.setJobCompletedMessage(ResponseMessages.FILE_IMPORT_FAILED+e.getLocalizedMessage());
            log.error(e.fillInStackTrace());
            importJob.setStatus(JobStatus.ERROR);
        }
        importJob.setCompletedAt(LocalDateTime.now());
        importJobRepository.save(importJob);
        log.info("File import job completed with status " + importJob.getStatus().name());
    }


    @Async
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE,propagation = Propagation.REQUIRED)
    public void createAndSubmitFileExportAsyncJob(Long jobId) {
        String exportMessage;
        ExportJob exportJob = exportJobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.EXPORT_FAILED_INVALID_JOB_ID));
        try {
            List<Section> sectionList = sectionRepository.findActiveSectionsWithActiveGeologicalClasses();

            if(CollectionUtils.isEmpty(sectionList)){
                throw new ValidationFailedException(ExceptionMessages.VAL_EXPORT_FAILED_NO_SECTIONS);
            }

            byte[] bytes = fileHandler.exportFile(sectionList);

            exportJob.setFileName(UtilMethods.determineFileName());
            exportJob.setFile(bytes);
            exportJob.setStatus(JobStatus.DONE);
            exportMessage = ResponseMessages.FILE_EXPORT_SUCCESS;
        } catch (Exception e) {
            exportMessage = e.getLocalizedMessage();
            exportJob.setStatus(JobStatus.ERROR);
        }
        exportJob.setCompletedAt(LocalDateTime.now());
        exportJob.setJobCompletedMessage(exportMessage);
        exportJobRepository.save(exportJob);
    }
}