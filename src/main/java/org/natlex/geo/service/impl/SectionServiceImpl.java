package org.natlex.geo.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.natlex.geo.dto.*;
import org.natlex.geo.dto.generic.GenericResponse;
import org.natlex.geo.entity.GeologicalClass;
import org.natlex.geo.entity.Section;
import org.natlex.geo.exception.NoContentFoundException;
import org.natlex.geo.helper.IDtoToEntityMapper;
import org.natlex.geo.helper.IEntityToDtoMapper;
import org.natlex.geo.helper.IResponseGenerator;
import org.natlex.geo.repository.IGeoLogicalClassRepository;
import org.natlex.geo.repository.ISectionRepository;
import org.natlex.geo.service.ISectionService;
import org.natlex.geo.util.ExceptionMessages;
import org.natlex.geo.util.ResponseMessages;
import org.natlex.geo.util.Status;
import org.natlex.geo.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;


/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/11/24
 * Time: 11:01 PM
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SectionServiceImpl implements ISectionService {

    private final ISectionRepository sectionRepository;
    private final IGeoLogicalClassRepository geoLogicalClassRepository;
    private final IResponseGenerator responseGenerator;
    private final IEntityToDtoMapper entityToDtoMapper;
    private final IDtoToEntityMapper dtoToEntityMapper;

    @Transactional(isolation = Isolation.SERIALIZABLE,propagation = Propagation.REQUIRED)
    @Override
    public GenericResponse addSection(SectionRequest sectionRequest) throws Exception {

        int sectionNumber =  ValidationUtils.validateSectionNameAndExtractNumber(sectionRequest.getName());
        Optional<Section> optionalSection = sectionRepository.findSectionBySectionIdAndStatusNot(sectionNumber, Status.DELETED);
        String responseMessageDescription;
        Section finalSection;
        if(optionalSection.isPresent()){
            finalSection = optionalSection.get();
            responseMessageDescription = ResponseMessages.SECTION_ALREADY_EXISTS_GL_UPDATED;
        }else {
            responseMessageDescription = ResponseMessages.SECTION_CREATED;
            finalSection =  dtoToEntityMapper.mapSectionDtoToEntity(sectionRequest);
        }
        ArrayList<String> notMatchingGlClass = new ArrayList<>();
        Set<GeologicalClassRequestDto> geologicalClassSetRequest = sectionRequest.getGeologicalClass();

        if(!CollectionUtils.isEmpty(geologicalClassSetRequest)){
            mapGeologicalClassesWithSection(geologicalClassSetRequest, finalSection, notMatchingGlClass);
        }

        if(!notMatchingGlClass.isEmpty()){
            responseMessageDescription = String.format(ResponseMessages.SECTION_CREATED_WITH_WARNING , Arrays.toString(notMatchingGlClass.toArray()));
        }
       return responseGenerator.generateSuccessResponse(
               entityToDtoMapper.mapSectionToSectionResponseDtoWithChildElements(sectionRepository.save(finalSection)),
               responseMessageDescription,
               HttpStatus.CREATED
       );
    }

    private void mapGeologicalClassesWithSection(Set<GeologicalClassRequestDto> geologicalClass, Section finalSection, List<String> notMatchingGlClass) {
        int sectionNumber = finalSection.getSectionId();
        for (GeologicalClassRequestDto gl : geologicalClass){
            if(StringUtils.hasText(gl.getCode()) && StringUtils.hasText(gl.getName())){
                if(ValidationUtils.validateNameAndCodeAgainstSectionNumber(gl.getName() , gl.getCode() , sectionNumber)) {
                    Optional<GeologicalClass> optionalGeologicalClass = geoLogicalClassRepository
                            .findGeologicalClassByCodeAndStatusNot(gl.getCode(), Status.DELETED);
                    if (optionalGeologicalClass.isPresent()) {
                        finalSection.getGeologicalClasses().add(optionalGeologicalClass.get());
                    } else {
                        finalSection
                                .getGeologicalClasses()
                                .add(dtoToEntityMapper.mapGeologicalClassDtoToEntity(gl));
                    }
                }else {
                    notMatchingGlClass.add(gl.getCode());
                }
            }
        }
    }

    @Transactional(readOnly = true)
    @Override
    public GenericResponse getAllSections() throws Exception {
        List<Section> allSectionByStatusNot = sectionRepository
                .findAllSectionByStatusNot(Status.DELETED);
        if (CollectionUtils.isEmpty(allSectionByStatusNot)) {
            throw new NoContentFoundException(ExceptionMessages.NO_CONTENT);
        }

        return responseGenerator.generateSuccessResponse(
                entityToDtoMapper.mapSectionEntityListToSectionResponseDtoListWithChildElements(allSectionByStatusNot),
                ResponseMessages.SECTION_FOUND,
                HttpStatus.OK
        );

    }

    @Transactional(isolation = Isolation.SERIALIZABLE,propagation = Propagation.REQUIRED)
    @Override
    public GenericResponse deleteSection(String id) throws Exception {

        Section section = sectionRepository
                .findSectionByIdAndStatusNot(id,Status.DELETED)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.SECTION_NOT_FOUND));
        section.setStatus(Status.DELETED);

        return responseGenerator.generateSuccessResponse(
                entityToDtoMapper.mapSectionToSectionResponseDto( sectionRepository.save(section)),
                ResponseMessages.SECTION_DELETED,
                HttpStatus.OK
        );

    }

    @Transactional(readOnly = true)
    @Override
    public GenericResponse getAllSectionsByGeoLocCode(String code) throws Exception {

        List<Section> sections = sectionRepository.findActiveSectionsWithActiveGeologicalClassesByGeoLocCode(code);

        if(sections.isEmpty()){
            throw new NoContentFoundException(ExceptionMessages.NO_CONTENT);
        }

        return responseGenerator.generateSuccessResponse(
                entityToDtoMapper.mapSectionEntityListToSectionResponseDtoListWithChildElements(sections),
                ResponseMessages.SECTION_FOUND,
                HttpStatus.OK
        );
    }
    @Transactional(isolation = Isolation.SERIALIZABLE,propagation = Propagation.REQUIRED)
    @Override
    public GenericResponse updateSection(SectionUpdateRequest sectionUpdateRequest) throws Exception {

        Section section = sectionRepository.findSectionByIdAndStatusNot(sectionUpdateRequest.getId(), Status.DELETED)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.SECTION_NOT_FOUND));

        if(Objects.nonNull(sectionUpdateRequest.getStatus())){
            section.setStatus(sectionUpdateRequest.getStatus());
        }
        List<String> notFoundGlcCode = new ArrayList<>();

        if(!CollectionUtils.isEmpty(sectionUpdateRequest.getGeologicalClass())){
            mapGeologicalClassesWithSection(sectionUpdateRequest.getGeologicalClass() , section ,notFoundGlcCode );
        }
        section.setUpdatedAt(LocalDateTime.now());
        Section updatedSection = sectionRepository.save(section);

        String responseDes = notFoundGlcCode.isEmpty() ? ResponseMessages.SECTION_UPDATED : String.format(ResponseMessages.SECTION_UPDATED_WITH_WARNING , Arrays.toString(notFoundGlcCode.toArray()));

        return responseGenerator.generateSuccessResponse(
                entityToDtoMapper.mapSectionToSectionResponseDtoWithChildElements(updatedSection),
                responseDes,
                HttpStatus.OK
        );
    }
}
