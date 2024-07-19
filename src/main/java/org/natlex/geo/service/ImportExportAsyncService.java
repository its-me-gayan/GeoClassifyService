package org.natlex.geo.service;

import org.natlex.geo.dto.generic.GenericResponse;

import java.io.InputStream;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/12/24
 * Time: 6:21 PM
 */
public interface ImportExportAsyncService {
    void createAndSubmitFileImportAsyncJob(InputStream inputStream,Long jobId);
    void createAndSubmitFileExportAsyncJob(Long jobId);
}
