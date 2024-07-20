package org.natlex.geo.service;

import org.natlex.geo.dto.ExportJobResultDto;
import org.natlex.geo.dto.generic.GenericResponse;

import java.io.InputStream;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/12/24
 * Time: 6:21 PM
 */
public interface ImportExportService {
    GenericResponse importFile(InputStream inputStream,String originalName)throws Exception;
    GenericResponse initiateExportJob()throws Exception;
    GenericResponse getImportJobStatusById(Long jobId)throws Exception;

    ExportJobResultDto getExportedFileByJobId(Long jobId) throws Exception;
    GenericResponse getExportJobByJobId(Long jobId) throws Exception;
}
