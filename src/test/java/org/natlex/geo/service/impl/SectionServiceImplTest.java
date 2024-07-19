package org.natlex.geo.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natlex.geo.dto.SectionRequest;
import org.natlex.geo.dto.SectionUpdateRequest;
import org.natlex.geo.dto.generic.GenericResponse;
import org.natlex.geo.entity.Section;
import org.natlex.geo.exception.NoContentFoundException;
import org.natlex.geo.exception.ValidationFailedException;
import org.natlex.geo.helper.DtoToEntityMapper;
import org.natlex.geo.helper.EntityToDtoMapper;
import org.natlex.geo.helper.ResponseGenerator;
import org.natlex.geo.repository.GeoLogicalClassRepository;
import org.natlex.geo.repository.SectionRepository;
import org.natlex.geo.service.SectionService;
import org.natlex.geo.util.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/18/24
 * Time: 11:56 PM
 */
@ExtendWith(MockitoExtension.class)
class SectionServiceImplTest {

    @Mock
    private SectionRepository sectionRepository;

    @Spy
    private  ResponseGenerator responseGenerator;

    @Spy
    private  EntityToDtoMapper entityToDtoMapper;

    @Spy
    private  DtoToEntityMapper dtoToEntityMapper;
    @InjectMocks
    public SectionServiceImpl sectionService;

    private Section section;

    @BeforeEach
    public void setUp(){
        section = Section.builder()
                .id(UUID.randomUUID().toString())
                .sectionId(10)
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .name("Section 10")
                .build();
    }

    @Test
    void test_addSection_Success() throws Exception {
            Mockito.when(sectionRepository.findSectionBySectionIdAndStatusNot(Mockito.anyInt() , Mockito.any()))
                    .thenReturn(Optional.empty());

            Mockito.when(sectionRepository.save(Mockito.any()))
                    .thenReturn(section);

            SectionRequest sectionRequest = SectionRequest
                    .builder()
                    .name("section 10")
                    .build();

            GenericResponse genericResponse = sectionService.addSection(sectionRequest);

            assertNotNull(genericResponse);
            assertEquals("Operation executed successfully",genericResponse.getResponseMessage());
            assertEquals("Section created successfully",genericResponse.getResponseDescription());
            assertEquals("000",genericResponse.getResponseCode());
            assertEquals(HttpStatus.CREATED.value(),genericResponse.getHttpStatusCode());
            assertTrue(genericResponse.isSuccess());
            assertNotNull(genericResponse.getData());


    }

    @Test
    void test_addSection_Failed_SectionNameFormat() {
        SectionRequest sectionRequest = SectionRequest
                .builder()
                .name("section xy")
                .build();

        ValidationFailedException ex = assertThrows(ValidationFailedException.class,
                () -> sectionService.addSection(sectionRequest)
        );
        assertEquals("Invalid section name format: "+sectionRequest.getName(),ex.getLocalizedMessage() );

    }

    @Test
    void test_getAllSections_Success() throws Exception {

        Mockito.when(sectionRepository.findAllSectionByStatusNot(Mockito.any()))
                        .thenReturn(
                                Collections.singletonList(section)
                        );

        GenericResponse genericResponse = sectionService.getAllSections();

        assertNotNull(genericResponse);
        assertEquals("Operation executed successfully",genericResponse.getResponseMessage());
        assertEquals("Sections found and attached to response",genericResponse.getResponseDescription());
        assertEquals("000",genericResponse.getResponseCode());
        assertEquals(HttpStatus.OK.value(),genericResponse.getHttpStatusCode());
        assertTrue(genericResponse.isSuccess());
        assertNotNull(genericResponse.getData());
    }

    @Test
    void test_getAllSections_NoContentFound() {

        Mockito.when(sectionRepository.findAllSectionByStatusNot(Mockito.any()))
                .thenReturn(Mockito.anyList());
        NoContentFoundException ex = assertThrows(NoContentFoundException.class, () -> sectionService.getAllSections());
        assertEquals("No content found" , ex.getLocalizedMessage());
    }

    @Test
    void test_deleteSection_Success() throws Exception {
        Mockito.when(sectionRepository.save(Mockito.any()))
                .thenReturn(section);

        Mockito.when(sectionRepository.findSectionByIdAndStatusNot(Mockito.any(),Mockito.any()))
                .thenReturn(Optional.of(Section.builder()
                        .sectionId(10)
                        .id("1ca62bd0-956c-4dfb-941e-e335280ac5cf")
                        .createdAt(LocalDateTime.now())
                        .name("Section 1")
                        .status(Status.ACTIVE).build()));
        GenericResponse genericResponse = sectionService.deleteSection("1ca62bd0-956c-4dfb-941e-e335280ac5cf");

        assertNotNull(genericResponse);
        assertEquals("Operation executed successfully",genericResponse.getResponseMessage());
        assertEquals("Section deleted successfully",genericResponse.getResponseDescription());
        assertEquals("000",genericResponse.getResponseCode());
        assertEquals(HttpStatus.OK.value(),genericResponse.getHttpStatusCode());
        assertTrue(genericResponse.isSuccess());
        assertNotNull(genericResponse.getData());
    }

    @Test
    void test_deleteSection_Failed_SectionNotFound(){

        Mockito.when(sectionRepository.findSectionByIdAndStatusNot(Mockito.any(),Mockito.any()))
                .thenReturn(Optional.empty());
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                sectionService.deleteSection("1ca62bd0-956c-4dfb-941e-e335280ac5cf")
        );
        assertEquals("Provided Section is not available in the system" , ex.getLocalizedMessage());
    }

    @Test
    void test_getAllSectionsByGeoLocCode_Success() throws Exception {
        Mockito.when(sectionRepository.findActiveSectionsWithActiveGeologicalClassesByGeoLocCode("GL01"))
                .thenReturn(Collections.singletonList(section));

        GenericResponse genericResponse = sectionService.getAllSectionsByGeoLocCode("GL01");

        assertNotNull(genericResponse);
        assertEquals("Operation executed successfully",genericResponse.getResponseMessage());
        assertEquals("Sections found and attached to response",genericResponse.getResponseDescription());
        assertEquals("000",genericResponse.getResponseCode());
        assertEquals(HttpStatus.OK.value(),genericResponse.getHttpStatusCode());
        assertTrue(genericResponse.isSuccess());
        assertNotNull(genericResponse.getData());
    }

    @Test
    void test_getAllSectionsByGeoLocCode_NoContentFound() throws Exception {
        Mockito.when(sectionRepository.findActiveSectionsWithActiveGeologicalClassesByGeoLocCode("GL01"))
                .thenReturn(Collections.emptyList());

        NoContentFoundException ex = assertThrows(NoContentFoundException.class, () -> sectionService.getAllSectionsByGeoLocCode("GL01"));
        assertEquals("No content found" , ex.getLocalizedMessage());
    }

    @Test
    void test_updateSection_Success() throws Exception {
        section.setId("1ca62bd0-956c-4dfb-941e-e335280ac5cf");
        Mockito.when(sectionRepository.findSectionByIdAndStatusNot(Mockito.any() , Mockito.any()))
                .thenReturn(Optional.of(section));


        Mockito.when(sectionRepository.save(Mockito.any()))
                .thenReturn(section);
        SectionUpdateRequest sectionRequest = SectionUpdateRequest
                .builder()
                .id("1ca62bd0-956c-4dfb-941e-e335280ac5cf")
                .status(Status.INACTIVE)
                .build();

        GenericResponse genericResponse = sectionService.updateSection(sectionRequest);
        assertNotNull(genericResponse);
        assertEquals("Operation executed successfully",genericResponse.getResponseMessage());
        assertEquals("Section Update success",genericResponse.getResponseDescription());
        assertEquals("000",genericResponse.getResponseCode());
        assertEquals(HttpStatus.OK.value(),genericResponse.getHttpStatusCode());
        assertTrue(genericResponse.isSuccess());
        assertNotNull(genericResponse.getData());
    }

    @Test
    void test_updateSection_Failed_SectionIdNotFound() throws Exception {
        Mockito.when(sectionRepository.findSectionByIdAndStatusNot(Mockito.any() , Mockito.any()))
                .thenReturn(Optional.empty());

        SectionUpdateRequest sectionRequest = SectionUpdateRequest
                .builder()
                .id("1ca62bd0-956c-4dfb-941e-e335280ac5cf")
                .status(Status.INACTIVE)
                .build();
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> sectionService.updateSection(sectionRequest));
        assertEquals("Provided Section is not available in the system" , ex.getLocalizedMessage());
    }
}