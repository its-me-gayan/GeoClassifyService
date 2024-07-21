package org.natlex.geo.service;

import org.natlex.geo.dto.generic.GenericResponse;

import java.io.InputStream;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/12/24
 * Time: 6:21 PM
 * Interface defining the contract for asynchronous import and export operations.
 */
public interface ImportExportAsyncService {

    /**
     * Creates and submits an asynchronous job to import sections from an Excel file.
     *
     * @param inputStream the input stream of the Excel file
     * @param jobId the ID of the import job
     */
    void createAndSubmitFileImportAsyncJob(InputStream inputStream,Long jobId);

    /**
     * Creates and submits an asynchronous job to export sections and geological classes to an Excel file.
     *
     * @param jobId the ID of the export job
     */
    void createAndSubmitFileExportAsyncJob(Long jobId);
}
