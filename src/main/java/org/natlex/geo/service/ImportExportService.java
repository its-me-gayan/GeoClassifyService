package org.natlex.geo.service;

import org.natlex.geo.dto.ExportJobResultDto;
import org.natlex.geo.dto.generic.GenericResponse;

import java.io.InputStream;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/12/24
 * Time: 6:21 PM
 *  Interface defining the contract for import and export operations.
 */
public interface ImportExportService {

    /**
     * Imports sections from an Excel file.
     * @param inputStream the input stream of the Excel file
     * @param originalName the original name of the Excel file
     * @return a generic response indicating the result of the import operation
     * @throws Exception if an error occurs during the import process
     */
    GenericResponse importFile(InputStream inputStream,String originalName)throws Exception;

    /**
     * Initiates an export job for all available sections and geological classes.
     * @return a generic response indicating the result of the export initiation
     * @throws Exception if an error occurs during the export initiation process
     */
    GenericResponse initiateExportJob()throws Exception;

    /**
     * Retrieves the status of an import job by its ID.
     * @param jobId the ID of the import job
     * @return a generic response containing the import job status
     * @throws Exception if an error occurs during the status retrieval process
     */
    GenericResponse getImportJobStatusById(Long jobId)throws Exception;

    /**
     * Retrieves the exported file associated with a specific job ID.
     * @param jobId the ID of the export job
     * @return an ExportJobResultDto containing the exported file and related information
     * @throws Exception if an error occurs during the file retrieval process
     */

    ExportJobResultDto getExportedFileByJobId(Long jobId) throws Exception;

    /**
     * Retrieves the details of an export job by its ID.
     * @param jobId the ID of the export job
     * @return a generic response containing the export job details
     * @throws Exception if an error occurs during the job details retrieval process
     */
    GenericResponse getExportJobByJobId(Long jobId) throws Exception;
}
