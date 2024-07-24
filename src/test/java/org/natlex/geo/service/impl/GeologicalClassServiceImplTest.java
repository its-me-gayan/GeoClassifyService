package org.natlex.geo.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natlex.geo.dto.GeologicalClassRequestDto;
import org.natlex.geo.dto.GeologicalClassUpdateRequest;
import org.natlex.geo.dto.generic.GenericResponse;
import org.natlex.geo.entity.GeologicalClass;
import org.natlex.geo.entity.Section;
import org.natlex.geo.exception.EntityExistException;
import org.natlex.geo.exception.NoContentFoundException;
import org.natlex.geo.exception.ValidationFailedException;
import org.natlex.geo.helper.impl.DtoToEntityMapper;
import org.natlex.geo.helper.impl.EntityToDtoMapper;
import org.natlex.geo.helper.impl.ResponseGenerator;
import org.natlex.geo.repository.IGeoLogicalClassRepository;
import org.natlex.geo.util.Status;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/19/24
 * Time: 11:15 PM
 */
@ExtendWith(MockitoExtension.class)
class GeologicalClassServiceImplTest {

    @Mock
    private IGeoLogicalClassRepository geoLogicalClassRepository;
    @Spy
    private ResponseGenerator responseGenerator;

    @Spy
    private  EntityToDtoMapper entityToDtoMapper;

    @Spy
    private  DtoToEntityMapper dtoToEntityMapper;

    @InjectMocks
    private GeologicalClassServiceImpl geologicalClassService;
   private GeologicalClass geologicalClass;
private GeologicalClassRequestDto classRequestDto;
    @BeforeEach
    void setUp() {
         geologicalClass = GeologicalClass.builder()
                .classNumber(9)
                .id(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .name("geo-class 39")
                .code("GL39")
                .build();

         classRequestDto = GeologicalClassRequestDto
                .builder()
                .name("geo-class 39")
                .code("GL39")
                .build();
    }

    @Test
    void test_addGeologicalClass_Success() throws Exception {

        Mockito.when( geoLogicalClassRepository.existsGeologicalClassByCodeAndStatusNot(Mockito.any(),Mockito.any()))
                .thenReturn(Boolean.FALSE);

        Mockito.when(geoLogicalClassRepository.save(Mockito.any()))
                .thenReturn(geologicalClass);


        GenericResponse genericResponse = geologicalClassService.addGeologicalClass(classRequestDto);
        assertNotNull(genericResponse);
        assertEquals("Operation executed successfully",genericResponse.getResponseMessage());
        assertEquals("GeologicalClass created successfully",genericResponse.getResponseDescription());
        assertEquals("000",genericResponse.getResponseCode());
        assertEquals(HttpStatus.CREATED.value(),genericResponse.getHttpStatusCode());
        assertTrue(genericResponse.isSuccess());
        assertNotNull(genericResponse.getData());
    }

    @Test
    void test_addGeologicalClass_Failed_GLClassAlreadyExists() throws Exception {

        Mockito.when( geoLogicalClassRepository.existsGeologicalClassByCodeAndStatusNot(Mockito.any(),Mockito.any()))
                .thenReturn(Boolean.TRUE);

        EntityExistException ex = assertThrows(EntityExistException.class, () ->
                geologicalClassService.addGeologicalClass(classRequestDto));

        assertEquals("GeologicalClass already exists in the system",ex.getLocalizedMessage());

    }
    @Test
    void test_addGeologicalClass_Failed_GLClassNameAndCodeNotMatch() throws Exception {

        Mockito.when( geoLogicalClassRepository.existsGeologicalClassByCodeAndStatusNot(Mockito.any(),Mockito.any()))
                .thenReturn(Boolean.FALSE);
        classRequestDto.setCode("GL37");
        ValidationFailedException ex = assertThrows(ValidationFailedException.class, () ->
                geologicalClassService.addGeologicalClass(classRequestDto));

        assertEquals("Validation failure - GeologicalClass name and code is different",ex.getLocalizedMessage());

    }

    @Test
    void test_getAllGeologicalClass_Success() throws Exception {
        Mockito.when(geoLogicalClassRepository.findAllByStatusNot(Mockito.any()))
                .thenReturn(Collections.singletonList(geologicalClass));
        GenericResponse genericResponse = geologicalClassService.getAllGeologicalClass();

        assertNotNull(genericResponse);
        assertEquals("Operation executed successfully",genericResponse.getResponseMessage());
        assertEquals("GeologicalClass found attached to response",genericResponse.getResponseDescription());
        assertEquals("000",genericResponse.getResponseCode());
        assertEquals(HttpStatus.OK.value(),genericResponse.getHttpStatusCode());
        assertTrue(genericResponse.isSuccess());
        assertNotNull(genericResponse.getData());
    }
    @Test
    void test_getAllGeologicalClass_NoContentFound() {
        Mockito.when(geoLogicalClassRepository.findAllByStatusNot(Mockito.any()))
                .thenReturn(Collections.emptyList());

        NoContentFoundException ex = assertThrows(NoContentFoundException.class, () ->
                geologicalClassService.getAllGeologicalClass());

        assertEquals("No content found",ex.getLocalizedMessage());
    }

    @Test
    void test_getGeologicalClassByCode_Success() throws Exception {
        Mockito.when(geoLogicalClassRepository.findGeologicalClassByCodeAndStatusNot(Mockito.any(),Mockito.any()))
                .thenReturn(Optional.of(geologicalClass));

        GenericResponse genericResponse = geologicalClassService.getGeologicalClassByCode("GL39");
        assertNotNull(genericResponse);
        assertEquals("Operation executed successfully",genericResponse.getResponseMessage());
        assertEquals("GeologicalClass found attached to response",genericResponse.getResponseDescription());
        assertEquals("000",genericResponse.getResponseCode());
        assertEquals(HttpStatus.OK.value(),genericResponse.getHttpStatusCode());
        assertTrue(genericResponse.isSuccess());
        assertNotNull(genericResponse.getData());
    }
    @Test
    void test_getGeologicalClassByCode_NoGeologicalClassFound() throws Exception {
        Mockito.when(geoLogicalClassRepository.findGeologicalClassByCodeAndStatusNot(Mockito.any(),Mockito.any()))
                .thenReturn(Optional.empty());
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                geologicalClassService.getGeologicalClassByCode("GL39"));

        assertEquals("Provided GeologicalClass not available in the system",ex.getLocalizedMessage());
    }

    @Test
    void test_updateGeologicalClass_Success() throws Exception {
        geologicalClass.setId("a7496d3e-09c6-4611-9a6b-fd51315377fa");
        Mockito.when(geoLogicalClassRepository.findByIdAndStatusNot(Mockito.any(),Mockito.any()))
                .thenReturn(Optional.of(geologicalClass));

        Mockito.when(geoLogicalClassRepository.save(Mockito.any()))
                .thenReturn(geologicalClass);

        GeologicalClassUpdateRequest request = GeologicalClassUpdateRequest.builder()
                .id("a7496d3e-09c6-4611-9a6b-fd51315377fa")
                .status(Status.ACTIVE)
                .name("geo-class 49")
                .code("GL49").build();
        GenericResponse genericResponse = geologicalClassService.updateGeologicalClass(request);
        assertNotNull(genericResponse);
        assertEquals("Operation executed successfully",genericResponse.getResponseMessage());
        assertEquals("GeologicalClass update success",genericResponse.getResponseDescription());
        assertEquals("000",genericResponse.getResponseCode());
        assertEquals(HttpStatus.OK.value(),genericResponse.getHttpStatusCode());
        assertTrue(genericResponse.isSuccess());
        assertNotNull(genericResponse.getData());
    }

    @Test
    void test_updateGeologicalClass_Failed_IdNotFound() throws Exception {
        Mockito.when(geoLogicalClassRepository.findByIdAndStatusNot(Mockito.any(),Mockito.any()))
                .thenReturn(Optional.empty());

        GeologicalClassUpdateRequest request = GeologicalClassUpdateRequest.builder()
                .id("a7496d3e-09c6-4611-9a6b-fd51315377fa")
                .status(Status.ACTIVE)
                .name("geo-class 49")
                .code("GL49").build();

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                geologicalClassService.updateGeologicalClass(request));
        assertEquals("Provided GeologicalClass not available in the system",ex.getLocalizedMessage());
    }
    @Test
    void test_updateGeologicalClass_Failed_BindWithASection() throws Exception {
        geologicalClass.setSections(Collections.singleton(Section.builder()
                .id(UUID.randomUUID().toString())
                .sectionId(10)
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .name("Section 10")
                .build()));

        Mockito.when(geoLogicalClassRepository.findByIdAndStatusNot(Mockito.any(),Mockito.any()))
                .thenReturn(Optional.of(geologicalClass));

        GeologicalClassUpdateRequest request = GeologicalClassUpdateRequest.builder()
                .id("a7496d3e-09c6-4611-9a6b-fd51315377fa")
                .status(Status.ACTIVE)
                .name("geo-class 49")
                .code("GL49").build();

        ValidationFailedException ex = assertThrows(ValidationFailedException.class, () ->
                geologicalClassService.updateGeologicalClass(request));
        assertEquals("Cannot proceed requested geologicalClass as this is already bound with a section",ex.getLocalizedMessage());
    }

    @Test
    void test_updateGeologicalClass_Failed_NameAndCodeNotMatching() throws Exception {

        Mockito.when(geoLogicalClassRepository.findByIdAndStatusNot(Mockito.any(),Mockito.any()))
                .thenReturn(Optional.of(geologicalClass));

        GeologicalClassUpdateRequest request = GeologicalClassUpdateRequest.builder()
                .id("a7496d3e-09c6-4611-9a6b-fd51315377fa")
                .status(Status.ACTIVE)
                .name("geo-class 49")
                .code("GL59").build();

        ValidationFailedException ex = assertThrows(ValidationFailedException.class, () ->
                geologicalClassService.updateGeologicalClass(request));
        assertEquals("Validation failure - GeologicalClass name and code is different",ex.getLocalizedMessage());
    }


    @Test
    void test_deleteGeologicalClass_Success() throws Exception {
        Mockito.when(geoLogicalClassRepository.findGeologicalClassByCodeAndStatusNot(Mockito.any(),Mockito.any()))
                .thenReturn(Optional.of(geologicalClass));

        Mockito.when(geoLogicalClassRepository.save(Mockito.any()))
                .thenReturn(geologicalClass);
        GenericResponse genericResponse = geologicalClassService.deleteGeologicalClass("GL39");
        assertNotNull(genericResponse);
        assertEquals("Operation executed successfully",genericResponse.getResponseMessage());
        assertEquals("GeologicalClass deleted successfully",genericResponse.getResponseDescription());
        assertEquals("000",genericResponse.getResponseCode());
        assertEquals(HttpStatus.OK.value(),genericResponse.getHttpStatusCode());
        assertTrue(genericResponse.isSuccess());
        assertNotNull(genericResponse.getData());
    }

    @Test
    void test_deleteGeologicalClass_Failed_CodeNotFound()   {
        Mockito.when(geoLogicalClassRepository.findGeologicalClassByCodeAndStatusNot(Mockito.any(),Mockito.any()))
                .thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                geologicalClassService.deleteGeologicalClass("GL39"));

        assertEquals("Provided GeologicalClass not available in the system",ex.getLocalizedMessage());
    }

    @Test
    void test_deleteGeologicalClass_Failed_BindWithASection()   {
        geologicalClass.setSections(Collections.singleton(Section.builder()
                .id(UUID.randomUUID().toString())
                .sectionId(10)
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .name("Section 10")
                .build()));
        Mockito.when(geoLogicalClassRepository.findGeologicalClassByCodeAndStatusNot(Mockito.any(),Mockito.any()))
                .thenReturn(Optional.of(geologicalClass));

        EntityExistException ex = assertThrows(EntityExistException.class, () ->
                geologicalClassService.deleteGeologicalClass("GL39"));

        assertEquals("Cannot proceed requested geologicalClass as this is already bound with a section",ex.getLocalizedMessage());
    }
}