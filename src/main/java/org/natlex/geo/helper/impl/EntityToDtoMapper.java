package org.natlex.geo.helper.impl;

import org.natlex.geo.dto.GeologicalClassResponseDto;
import org.natlex.geo.dto.JobDetailResponseDto;
import org.natlex.geo.dto.SectionResponse;
import org.natlex.geo.entity.ExportJob;
import org.natlex.geo.entity.GeologicalClass;
import org.natlex.geo.entity.ImportJob;
import org.natlex.geo.entity.Section;
import org.natlex.geo.helper.IEntityToDtoMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/14/24
 * Time: 7:49 PM
 */

@Component
public class EntityToDtoMapper implements IEntityToDtoMapper {

    public SectionResponse mapSectionToSectionResponseDto(Section section){
        return SectionResponse.builder()
                .status(section.getStatus())
                .id(section.getId())
                .sectionId(section.getSectionId())
                .name(section.getName())
                .build();
    }
    public SectionResponse mapSectionToSectionResponseDtoWithChildElements(Section section){
        return SectionResponse.builder()
                .status(section.getStatus())
                .id(section.getId())
                .sectionId(section.getSectionId())
                .name(section.getName())
                .geologicalClass(
                        mapGeologicalClassToGeologicalClassResponseDtoList(section.getGeologicalClasses())
                )
                .build();
    }
    public Set<GeologicalClassResponseDto> mapGeologicalClassToGeologicalClassResponseDtoList(Set<GeologicalClass> geologicalClassSet){
        if(CollectionUtils.isEmpty(geologicalClassSet)){
            return Collections.emptySet();
        }
        return geologicalClassSet.stream().map(this::mapGeologicalClassToDto).collect(Collectors.toSet());
    }

    public List<SectionResponse> mapSectionEntityListToSectionResponseDtoList(List<Section> sectionList){
        return sectionList
                .stream()
                .map(this::mapSectionToSectionResponseDto)
                .toList();
    }
    public List<SectionResponse> mapSectionEntityListToSectionResponseDtoListWithChildElements(List<Section> sectionList){
        return sectionList
                .stream()
                .map(this::mapSectionToSectionResponseDtoWithChildElements)
                .toList();
    }
    ///geo log class///////
    public GeologicalClassResponseDto mapGeologicalClassToDtoBasic(GeologicalClass geologicalClass){
        return GeologicalClassResponseDto.builder()
                .id(geologicalClass.getId())
                .name(geologicalClass.getName())
                .code(geologicalClass.getCode())
                .build();
    }
    public GeologicalClassResponseDto mapGeologicalClassToDto(GeologicalClass geologicalClass){
        return GeologicalClassResponseDto.builder()
                .id(geologicalClass.getId())
                .name(geologicalClass.getName())
                .status(geologicalClass.getStatus())
                .code(geologicalClass.getCode())
                .createdAt(geologicalClass.getCreatedAt())
                .updatedAt(geologicalClass.getUpdatedAt())
                .build();
    }

    public List<GeologicalClassResponseDto> mapGeologicalClassListToDtoList(List<GeologicalClass> geologicalClasses){
        return geologicalClasses.stream().map(this::mapGeologicalClassToDto).toList();
    }

    public JobDetailResponseDto mapImportJobEntityToDto(ImportJob importJob){
        return JobDetailResponseDto.builder()
                .jobCompletedMessage(importJob.getJobCompletedMessage())
                        .jobId(importJob.getId())
                        .status(importJob.getStatus())
                        .build();
    }

    public JobDetailResponseDto mapExportJobEntityToDto(ExportJob exportJob){
        return JobDetailResponseDto.builder()
                .jobCompletedMessage(exportJob.getJobCompletedMessage())
                .jobId(exportJob.getId())
                .status(exportJob.getStatus())
                .build();
    }
}
