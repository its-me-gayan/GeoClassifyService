package org.natlex.geo.helper.impl;

import org.apache.poi.util.StringUtil;
import org.natlex.geo.dto.GeologicalClassRequestDto;
import org.natlex.geo.dto.GeologicalClassUpdateRequest;
import org.natlex.geo.dto.SectionRequest;
import org.natlex.geo.entity.GeologicalClass;
import org.natlex.geo.entity.Section;
import org.natlex.geo.helper.IDtoToEntityMapper;
import org.natlex.geo.util.Status;
import org.natlex.geo.util.ValidationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/11/24
 * Time: 11:25 PM
 */
@Component
public class DtoToEntityMapper implements IDtoToEntityMapper {

    public GeologicalClass mapGeologicalClassDtoToEntity(GeologicalClassRequestDto geologicalClassRequest){
       return GeologicalClass.builder()
                .classNumber(ValidationUtils.extractGeologicalClassNumberFromCode(geologicalClassRequest.getCode()))
                .code(geologicalClassRequest.getCode())
                .name(geologicalClassRequest.getName())
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();
    }


    public void mapGeologicalClassUpdateDtoToEntity(GeologicalClassUpdateRequest updateRequest,GeologicalClass geologicalClass){

        geologicalClass.setClassNumber(ValidationUtils.extractGeologicalClassNumberFromCode(updateRequest.getCode()));
        geologicalClass.setUpdatedAt(LocalDateTime.now());

        if(StringUtils.hasText(updateRequest.getCode())){
            geologicalClass.setCode(updateRequest.getCode());
        }
        if(StringUtils.hasText(updateRequest.getName())){
            geologicalClass.setName(updateRequest.getName());
        }
        if(Objects.nonNull(updateRequest.getStatus())){
            geologicalClass.setStatus(updateRequest.getStatus());
        }

    }

    public GeologicalClass mapGeologicalClassDtoToEntity(String name , String code , int classNumber){
        return GeologicalClass.builder()
                .classNumber(classNumber)
                .code(code)
                .name(name)
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public Section mapSectionDtoToEntity(SectionRequest sectionRequest){
        return Section.builder()
                .sectionId(ValidationUtils.validateSectionNameAndExtractNumber(sectionRequest.getName()))
                .name(sectionRequest.getName())
                .geologicalClasses(new HashSet<>())
                .status(Status.ACTIVE)
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
    }
    public Section mapSectionDtoToEntity(String name , int sectionId){
        return Section.builder()
                .sectionId(sectionId)
                .name(name)
                .geologicalClasses(new HashSet<>())
                .status(Status.ACTIVE)
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
    }

}
