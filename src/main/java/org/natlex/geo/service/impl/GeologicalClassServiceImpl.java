package org.natlex.geo.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.natlex.geo.dto.GeologicalClassRequestDto;
import org.natlex.geo.dto.GeologicalClassUpdateRequest;
import org.natlex.geo.dto.generic.GenericResponse;
import org.natlex.geo.entity.GeologicalClass;
import org.natlex.geo.exception.EntityExistException;
import org.natlex.geo.exception.NoContentFoundException;
import org.natlex.geo.exception.ValidationFailedException;
import org.natlex.geo.helper.IDtoToEntityMapper;
import org.natlex.geo.helper.IEntityToDtoMapper;
import org.natlex.geo.helper.IResponseGenerator;
import org.natlex.geo.repository.IGeoLogicalClassRepository;
import org.natlex.geo.service.IGeologicalClassService;
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

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/12/24
 * Time: 2:37 PM
 */

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GeologicalClassServiceImpl implements IGeologicalClassService {

    private final IGeoLogicalClassRepository geoLogicalClassRepository;
    private final IResponseGenerator responseGenerator;
    private final IEntityToDtoMapper entityToDtoMapper;
    private final IDtoToEntityMapper dtoToEntityMapper;
    @Override
    @Transactional(propagation = Propagation.REQUIRED , isolation = Isolation.READ_COMMITTED,rollbackFor = SQLException.class)
    public GenericResponse addGeologicalClass(GeologicalClassRequestDto request) throws Exception {

        if(geoLogicalClassRepository.existsGeologicalClassByCodeAndStatusNot(request.getCode(), Status.DELETED)){
            throw new EntityExistException(ExceptionMessages.GL_CLASS_EXISTS);
        }

        if(!ValidationUtils.validateNameAndCode(request.getName(), request.getCode())){
            throw new ValidationFailedException(ExceptionMessages.VAL_GL_CLASS_NAME_CODE_NOT_MATCH);
        }

        GeologicalClass saveGeologicalClass = geoLogicalClassRepository
                .save(
                        dtoToEntityMapper.mapGeologicalClassDtoToEntity(request)
                );

        return responseGenerator.generateSuccessResponse(
                entityToDtoMapper.mapGeologicalClassToDto(saveGeologicalClass),
                ResponseMessages.GL_CLASS_CREATED,
                HttpStatus.CREATED
        );

    }
    @Override
    @Transactional(readOnly = true)
    public GenericResponse getAllGeologicalClass() throws Exception {
        List<GeologicalClass> allStatusNot = geoLogicalClassRepository.findAllByStatusNot(Status.DELETED);
        if(CollectionUtils.isEmpty(allStatusNot)){
            throw new NoContentFoundException(ExceptionMessages.NO_CONTENT);
        }
       return responseGenerator.generateSuccessResponse(
                entityToDtoMapper.mapGeologicalClassListToDtoList(allStatusNot),
                ResponseMessages.GL_CLASS_FOUND,
                HttpStatus.OK
        );
    }

    @Override
    @Transactional(readOnly = true)
    public GenericResponse getGeologicalClassByCode(String code) throws Exception {
        GeologicalClass geologicalClass = geoLogicalClassRepository
                .findGeologicalClassByCodeAndStatusNot(code, Status.DELETED)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.GL_CLASS_NOT_FOUND));

        return responseGenerator.generateSuccessResponse(
                entityToDtoMapper.mapGeologicalClassToDto(geologicalClass),
                ResponseMessages.GL_CLASS_FOUND,
                HttpStatus.OK
        );
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED , isolation = Isolation.READ_COMMITTED,rollbackFor = SQLException.class)
    public GenericResponse updateGeologicalClass(GeologicalClassUpdateRequest request) throws Exception {
        GeologicalClass geologicalClass = geoLogicalClassRepository
                .findByIdAndStatusNot(request.getId(), Status.DELETED)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.GL_CLASS_NOT_FOUND));

       if(!CollectionUtils.isEmpty(geologicalClass.getSections())){
           throw new ValidationFailedException(ExceptionMessages.VAL_GL_CLASS_ALREADY_BIND);
       }

       if(!ValidationUtils.validateNameAndCode(request.getName(), request.getCode())){
           throw new ValidationFailedException(ExceptionMessages.VAL_GL_CLASS_NAME_CODE_NOT_MATCH);
       }

        dtoToEntityMapper.mapGeologicalClassUpdateDtoToEntity(request,geologicalClass);

        GeologicalClass savedGeologicalClass = geoLogicalClassRepository.save(geologicalClass);

        return responseGenerator.generateSuccessResponse(
                entityToDtoMapper.mapGeologicalClassToDto(savedGeologicalClass),
                ResponseMessages.GL_CLASS_UPDATED,
                HttpStatus.OK
        );

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED , isolation = Isolation.READ_COMMITTED,rollbackFor = SQLException.class)
    public GenericResponse deleteGeologicalClass(String code) throws Exception {
        GeologicalClass geologicalClass = geoLogicalClassRepository.findGeologicalClassByCodeAndStatusNot(code, Status.DELETED)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.GL_CLASS_NOT_FOUND));
        if(!CollectionUtils.isEmpty(geologicalClass.getSections())){
            throw new EntityExistException(ExceptionMessages.VAL_GL_CLASS_ALREADY_BIND);
        }
        geologicalClass.setStatus(Status.DELETED);
        geologicalClass.setUpdatedAt(LocalDateTime.now());
        GeologicalClass deleted = geoLogicalClassRepository.save(geologicalClass);

        return responseGenerator.generateSuccessResponse(
                entityToDtoMapper.mapGeologicalClassToDtoBasic(deleted),
                ResponseMessages.GL_CLASS_DELETED,
                HttpStatus.OK
        );
    }
}
