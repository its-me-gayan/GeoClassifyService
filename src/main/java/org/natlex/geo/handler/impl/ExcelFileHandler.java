package org.natlex.geo.handler.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.natlex.geo.entity.GeologicalClass;
import org.natlex.geo.entity.Section;
import org.natlex.geo.exception.ValidationFailedException;
import org.natlex.geo.handler.IFileHandler;
import org.natlex.geo.helper.impl.DtoToEntityMapper;
import org.natlex.geo.repository.IGeoLogicalClassRepository;
import org.natlex.geo.repository.ISectionRepository;
import org.natlex.geo.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/15/24
 * Time: 1:09 PM
 */

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class ExcelFileHandler implements IFileHandler {

    private final ISectionRepository sectionRepository;
    private final IGeoLogicalClassRepository geoLogicalClassRepository;
    private final DtoToEntityMapper dtoToEntityMapper;

    @Override
    public List<Section> importFile(InputStream inputStream) throws Exception {
        String CUSTOM_LOG_NAME_IMP = "[ IMPORT-FILE ]";
        log.debug("{} Started excel ({}) file importing process", CUSTOM_LOG_NAME_IMP,getFileExtension());
        List<Section> sections = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Skip header
            if (Objects.nonNull(row.getCell(0)) && StringUtils.hasText(row.getCell(0).getStringCellValue())) {
                String sectionName = row.getCell(0).getStringCellValue();
                Section section = getSectionEntityFromId(sectionName);

                for (int i = 1; i < row.getLastCellNum(); i += 2) {
                    if ((Objects.nonNull(row.getCell(i)) && StringUtils.hasText(row.getCell(i).getStringCellValue())) &&
                            (Objects.nonNull(row.getCell(i + 1)) && StringUtils.hasText(row.getCell(i + 1).getStringCellValue()))) {

                        String name = row.getCell(i).getStringCellValue();
                        String code = row.getCell(i + 1).getStringCellValue();
                        int classId = (i + 1) / 2;

                        if (ValidationUtils.validateNameAndCode(sectionName , name, code, classId)) {
                            geoLogicalClassRepository
                                    .findGeologicalClassByCodeAndStatusNot(code, Status.DELETED)
                                    .ifPresentOrElse(
                                            section.getGeologicalClasses()::add,
                                            () -> {
                                                GeologicalClass geologicalClass = dtoToEntityMapper.mapGeologicalClassDtoToEntity(name, code, classId);
                                                section.getGeologicalClasses().add(geologicalClass);
                                            }
                                    );
                        } else {
                            log.info("{} Geological class code or name validation failed" , CUSTOM_LOG_NAME_IMP);
                        }
                    } else {
                        log.info("{} Geological class code or name empty or null and ignored" , CUSTOM_LOG_NAME_IMP);
                    }
                }
                sections.add(section);
                log.info("{} Section {} imported successfully" , CUSTOM_LOG_NAME_IMP, section.getName());
            } else {
                log.info("{} Section ID cell null or empty" , CUSTOM_LOG_NAME_IMP);
            }
        }
        workbook.close();
        log.debug("{} Finished excel ({}) file importing process", CUSTOM_LOG_NAME_IMP,getFileExtension());

        return sections;
    }

    @Override
    public byte[] exportFile(List<Section> sections) throws Exception {
        String CUSTOM_LOG_NAME_EXP = "[ EXPORT-FILE ]";
        log.debug("{} Started excel ({}) file exporting process", CUSTOM_LOG_NAME_EXP,getFileExtension());

        Integer maxGeoClassNumber = UtilMethods.findMaxGeoClassNumber(sections)
                .orElseThrow(() -> new ValidationFailedException(ExceptionMessages.EXPORT_FAILED_COLUMN_COUNT));

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sections");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Section name");
        int cellIndex = 1;
        for (int i = 1; i <= maxGeoClassNumber; i++) {
            header.createCell(cellIndex++).setCellValue("Class " + i + " name");
            header.createCell(cellIndex++).setCellValue("Class " + i + " code");
        }

        int rowIndex = 1;
        for (Section section : sections) {
            log.info("{} Exporting section - {}", CUSTOM_LOG_NAME_EXP,section.getName());
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(section.getName());

            for (GeologicalClass geoClass : section.getGeologicalClasses()) {
                if(geoClass.getClassNumber() > 16384) continue;

                log.info("{} Exporting Geological class - {}", CUSTOM_LOG_NAME_EXP,geoClass.getCode());

                int classNumber = geoClass.getClassNumber();
                classNumber = (classNumber*2) -1;
                row.createCell(classNumber).setCellValue(geoClass.getName());
                row.createCell(classNumber+1).setCellValue(geoClass.getCode());
            }
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        log.debug("{} Finished excel ({}) file exporting process", CUSTOM_LOG_NAME_EXP,getFileExtension());

        return outputStream.toByteArray();
    }

    @Override
    public String getFileExtension() {
        return "xlsx";
    }

    private Section getSectionEntityFromId(String sectionName) {
        int sectionNumber = ValidationUtils.validateSectionNameAndExtractNumber(sectionName);
        return sectionRepository.findSectionBySectionIdAndStatusNot(sectionNumber, Status.DELETED)
                .orElseGet(() -> dtoToEntityMapper.mapSectionDtoToEntity(sectionName, sectionNumber));
    }
}
