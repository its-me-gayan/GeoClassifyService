package org.natlex.geo.service;

import org.natlex.geo.dto.SectionRequest;
import org.natlex.geo.dto.SectionUpdateRequest;
import org.natlex.geo.dto.generic.GenericResponse;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/11/24
 * Time: 11:00 PM
 * Interface defining the contract for section-related operations.
 */
public interface ISectionService {

    /**
     * Adds a new section.
     * @param sectionRequest the request object containing section details
     * @return a generic response indicating the result of the operation
     * @throws Exception if an error occurs during the process
     */
    GenericResponse addSection(SectionRequest sectionRequest) throws Exception;

    /**
     * Retrieves all sections.
     * @return a generic response containing the list of all sections
     * @throws Exception if an error occurs during the retrieval process
     */
    GenericResponse getAllSections() throws Exception;

    /**
     * Deletes a section by its ID.
     * @param id the ID of the section to be deleted
     * @return a generic response indicating the result of the operation
     * @throws Exception if an error occurs during the deletion process
     */
    GenericResponse deleteSection(String id) throws Exception;

    /**
     * Retrieves all sections associated with a specific geological class code.
     * @param code the geological class code
     * @return a generic response containing the list of relevant sections
     * @throws Exception if an error occurs during the retrieval process
     */
    GenericResponse getAllSectionsByGeoLocCode(String code) throws Exception;

    /**
     * Updates an existing section.
     * @param sectionUpdateRequest the request object containing updated section details
     * @return a generic response indicating the result of the operation
     * @throws Exception if an error occurs during the update process
     */
    GenericResponse updateSection(SectionUpdateRequest sectionUpdateRequest) throws Exception;
}
