package org.natlex.geo.service;

import org.natlex.geo.dto.SectionRequest;
import org.natlex.geo.dto.SectionUpdateRequest;
import org.natlex.geo.dto.generic.GenericResponse;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/11/24
 * Time: 11:00 PM
 */
public interface SectionService {

    GenericResponse addSection(SectionRequest sectionRequest) throws Exception;
    GenericResponse getAllSections() throws Exception;
    GenericResponse deleteSection(String id) throws Exception;
    GenericResponse getAllSectionsByGeoLocCode(String code) throws Exception;
    GenericResponse updateSection(SectionUpdateRequest sectionUpdateRequest) throws Exception;
}
