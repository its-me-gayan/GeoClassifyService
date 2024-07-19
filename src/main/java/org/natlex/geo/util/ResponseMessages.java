package org.natlex.geo.util;

import java.util.Arrays;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/14/24
 * Time: 11:37 PM
 */
public class ResponseMessages {
    private ResponseMessages(){}
    private static final String WARNING_MZG = "and  %s GeologicalClass are not matching with provided section and ignored";

    public static final String REQUEST_PROC_FAILED ="Request Processing failed";
    public static final String REQUEST_PROC_FAILED_DUE_VAL ="Request failed due to field validations failures";
    public static final String OPERATION_EXECUTED ="Operation executed successfully";
    public static final String SECTION_DELETED ="Section deleted successfully";
    public static final String GL_CLASS_DELETED ="GeologicalClass deleted successfully";
    public static final String SECTION_FOUND ="Sections found and attached to response";
    public static final String GL_CLASS_FOUND ="GeologicalClass found attached to response";
    public static final String SECTION_UPDATED ="Section Update success";
    public static final String GL_CLASS_UPDATED ="GeologicalClass update success";
    public static final String SECTION_CREATED ="Section created successfully";
    public static final String GL_CLASS_CREATED ="GeologicalClass created successfully";
    public static final String SECTION_UPDATED_WITH_WARNING = SECTION_UPDATED + WARNING_MZG;
    public static final String SECTION_CREATED_WITH_WARNING = SECTION_CREATED+ WARNING_MZG;
    public static final String SECTION_ALREADY_EXISTS_GL_UPDATED = "Section already exists and updated the provided geologicalClasses";
    public static final String IMP_JOB_SUBMITTED = "Import job created a submitted";
    public static final String EXP_JOB_SUBMITTED = "Export job created a submitted";
    public static final String IMP_JOB_FOUND = "Import Job Found";
    public static final String EXP_JOB_FOUND = "Export Job Found";
    public static final String FILE_IMPORT_SUCCESS = "File Imported successfully";
    public static final String FILE_EXPORT_SUCCESS = "File Export success and xlsx file saved";
    public static final String FILE_EXPORT_FAILED = "File Export failed - ";
    public static final String FILE_IMPORT_FAILED = "File Import failed - ";

}