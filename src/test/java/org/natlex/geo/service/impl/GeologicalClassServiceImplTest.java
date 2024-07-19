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
import org.natlex.geo.dto.generic.GenericResponse;
import org.natlex.geo.entity.GeologicalClass;
import org.natlex.geo.exception.EntityExistException;
import org.natlex.geo.exception.NoContentFoundException;
import org.natlex.geo.exception.ValidationFailedException;
import org.natlex.geo.helper.DtoToEntityMapper;
import org.natlex.geo.helper.EntityToDtoMapper;
import org.natlex.geo.helper.ResponseGenerator;
import org.natlex.geo.repository.GeoLogicalClassRepository;
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
    private GeoLogicalClassRepository geoLogicalClassRepository;
    @Spy
    private  ResponseGenerator responseGenerator;

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
    void addGeologicalClass_Success_Test() throws Exception {

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
    void addGeologicalClass_Failed_GLClassAlreadyExists_Test() throws Exception {

        Mockito.when( geoLogicalClassRepository.existsGeologicalClassByCodeAndStatusNot(Mockito.any(),Mockito.any()))
                .thenReturn(Boolean.TRUE);

        EntityExistException ex = assertThrows(EntityExistException.class, () ->
                geologicalClassService.addGeologicalClass(classRequestDto));

        assertEquals("GeologicalClass already exists in the system",ex.getLocalizedMessage());

    }
    @Test
    void addGeologicalClass_Failed_GLClassNameAndCodeNotMatch_Test() throws Exception {

        Mockito.when( geoLogicalClassRepository.existsGeologicalClassByCodeAndStatusNot(Mockito.any(),Mockito.any()))
                .thenReturn(Boolean.FALSE);
        classRequestDto.setCode("GL37");
        ValidationFailedException ex = assertThrows(ValidationFailedException.class, () ->
                geologicalClassService.addGeologicalClass(classRequestDto));

        assertEquals("Validation failure - GeologicalClass name and code is different",ex.getLocalizedMessage());

    }

    @Test
    void getAllGeologicalClass_Success_Test() throws Exception {
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
    void getAllGeologicalClass_NoContentFound_Test() {
        Mockito.when(geoLogicalClassRepository.findAllByStatusNot(Mockito.any()))
                .thenReturn(Collections.emptyList());

        NoContentFoundException ex = assertThrows(NoContentFoundException.class, () ->
                geologicalClassService.getAllGeologicalClass());

        assertEquals("No content found",ex.getLocalizedMessage());
    }

    @Test
    void getGeologicalClassByCode_Success_Test() throws Exception {
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
    void getGeologicalClassByCode_NoGeologicalClassFound_Test() throws Exception {
        Mockito.when(geoLogicalClassRepository.findGeologicalClassByCodeAndStatusNot(Mockito.any(),Mockito.any()))
                .thenReturn(Optional.empty());
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                geologicalClassService.getGeologicalClassByCode("GL39"));

        assertEquals("Provided GeologicalClass not available in the system",ex.getLocalizedMessage());
    }

    @Test
    void updateGeologicalClass() {
    }

    @Test
    void deleteGeologicalClass() {
    }
}