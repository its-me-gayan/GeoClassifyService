package org.natlex.geo.util;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/14/24
 * Time: 11:30 PM
 */
public class ExceptionMessages {
    private ExceptionMessages(){}
    public static final String NO_CONTENT ="No content found";
    public static final String SECTION_NOT_FOUND ="Provided Section is not available in the system";
    public static final String GL_CLASS_NOT_FOUND ="Provided GeologicalClass not available in the system";
    public static final String GL_CLASS_EXISTS ="GeologicalClass already exists in the system";
    public static final String VAL_GL_CLASS_NAME_CODE_NOT_MATCH ="Validation failure - GeologicalClass name and code is different";
    public static final String VAL_GL_CLASS_ALREADY_BIND ="Cannot proceed requested geologicalClass as this is already bound with a section";
    public static final String JOB_NOT_FOUND ="Job not found - Invalid JobId";
    public static final String EXPORT_FAILED_COLUMN_COUNT ="Export Failed - Error detected while calculating the max column count for geologicalClasses";
    public static final String EXPORT_FAILED_INVALID_JOB_ID ="Export failed -> no valid job id found";
    public static final String IMPORT_FAILED_INVALID_JOB_ID ="Import failed -> no valid job id found";
    public static final String VAL_EXPORT_FAILED_NO_SECTIONS ="Export Failed - No valid sections are found to export";
    public static final String VAL_IMPORT_FAILED_NO_SECTIONS ="Import Failed - No valid sections are found to import";
}
