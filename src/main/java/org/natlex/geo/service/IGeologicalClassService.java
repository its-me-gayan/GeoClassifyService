package org.natlex.geo.service;

import org.natlex.geo.dto.GeologicalClassRequestDto;
import org.natlex.geo.dto.GeologicalClassUpdateRequest;
import org.natlex.geo.dto.generic.GenericResponse;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/12/24
 * Time: 2:37 PM
 * Interface defining the contract for geological class-related operations.
 */
public interface IGeologicalClassService {

    /**
     * Adds a new geological class.
     *
     * @param geologicalClassRequest the request object containing geological class details
     * @return a generic response indicating the result of the operation
     * @throws Exception if an error occurs during the process
     */
    GenericResponse addGeologicalClass(GeologicalClassRequestDto geologicalClassRequest)throws Exception;

    /**
     * Retrieves all geological classes.
     *
     * @return a generic response containing the list of all geological classes
     * @throws Exception if an error occurs during the retrieval process
     */
    GenericResponse getAllGeologicalClass()throws Exception;

    /**
     * Retrieves a geological class by its code.
     *
     * @param code the code of the geological class
     * @return a generic response containing the geological class details
     * @throws Exception if an error occurs during the retrieval process
     */
    GenericResponse getGeologicalClassByCode(String code)throws Exception;

    /**
     * Updates an existing geological class.
     *
     * @param geologicalClassUpdateRequest the request object containing updated geological class details
     * @return a generic response indicating the result of the operation
     * @throws Exception if an error occurs during the update process
     */
    GenericResponse updateGeologicalClass(GeologicalClassUpdateRequest geologicalClassUpdateRequest)throws Exception;

    /**
     * Deletes a geological class by its code.
     *
     * @param code the code of the geological class to be deleted
     * @return a generic response indicating the result of the operation
     * @throws Exception if an error occurs during the deletion process
     */
    GenericResponse deleteGeologicalClass(String code)throws Exception;
}
