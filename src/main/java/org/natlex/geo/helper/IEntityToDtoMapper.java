package org.natlex.geo.helper;

import org.natlex.geo.dto.GeologicalClassResponseDto;
import org.natlex.geo.dto.JobDetailResponseDto;
import org.natlex.geo.dto.SectionResponse;
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
public interface IEntityToDtoMapper {
    SectionResponse mapSectionToSectionResponseDto(Section section);
    SectionResponse mapSectionToSectionResponseDtoWithChildElements(Section section);
    List<SectionResponse> mapSectionEntityListToSectionResponseDtoList(List<Section> sectionList);
    List<SectionResponse> mapSectionEntityListToSectionResponseDtoListWithChildElements(List<Section> sectionList);
    GeologicalClassResponseDto mapGeologicalClassToDtoBasic(GeologicalClass geologicalClass);
    GeologicalClassResponseDto mapGeologicalClassToDto(GeologicalClass geologicalClass);
    Set<GeologicalClassResponseDto> mapGeologicalClassToGeologicalClassResponseDtoList(Set<GeologicalClass> geologicalClassSet);
    List<GeologicalClassResponseDto> mapGeologicalClassListToDtoList(List<GeologicalClass> geologicalClasses);

    JobDetailResponseDto mapImportJobEntityToDto(ImportJob importJob);
    JobDetailResponseDto mapExportJobEntityToDto(ExportJob exportJob);
}
