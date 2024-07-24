package org.natlex.geo.helper;

import org.natlex.geo.dto.*;
import org.natlex.geo.entity.ExportJob;
import org.natlex.geo.entity.GeologicalClass;
import org.natlex.geo.entity.ImportJob;
import org.natlex.geo.entity.Section;

import java.util.List;
import java.util.Set;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/24/24
 * Time: 10:24 PM
 */
public interface IDtoToEntityMapper {
    GeologicalClass mapGeologicalClassDtoToEntity(GeologicalClassRequestDto geologicalClassRequest);
    void mapGeologicalClassUpdateDtoToEntity(GeologicalClassUpdateRequest updateRequest, GeologicalClass geologicalClass);
    GeologicalClass mapGeologicalClassDtoToEntity(String name, String code, int classNumber);
    Section mapSectionDtoToEntity(SectionRequest sectionRequest);
    Section mapSectionDtoToEntity(String name, int sectionId);
}
