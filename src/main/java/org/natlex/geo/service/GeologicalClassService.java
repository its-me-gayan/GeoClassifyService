package org.natlex.geo.service;

import org.natlex.geo.dto.GeologicalClassRequestDto;
import org.natlex.geo.dto.GeologicalClassUpdateRequest;
import org.natlex.geo.dto.generic.GenericResponse;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/12/24
 * Time: 2:37 PM
 */
public interface GeologicalClassService {
    GenericResponse addGeologicalClass(GeologicalClassRequestDto geologicalClassRequest)throws Exception;
    GenericResponse getAllGeologicalClass()throws Exception;
    GenericResponse getGeologicalClassByCode(String code)throws Exception;
    GenericResponse updateGeologicalClass(GeologicalClassUpdateRequest geologicalClassUpdateRequest)throws Exception;
    GenericResponse deleteGeologicalClass(String code)throws Exception;
}
