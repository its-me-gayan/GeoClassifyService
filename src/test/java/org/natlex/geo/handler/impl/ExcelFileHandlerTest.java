package org.natlex.geo.handler.impl;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natlex.geo.entity.GeologicalClass;
import org.natlex.geo.entity.Section;
import org.natlex.geo.exception.ValidationFailedException;
import org.natlex.geo.helper.DtoToEntityMapper;
import org.natlex.geo.repository.GeoLogicalClassRepository;
import org.natlex.geo.repository.SectionRepository;
import org.natlex.geo.util.Status;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/20/24
 * Time: 12:38 AM
 */
@ExtendWith(MockitoExtension.class)
class ExcelFileHandlerTest {

    @Mock
    private  SectionRepository sectionRepository;
    @Mock
    private  GeoLogicalClassRepository geoLogicalClassRepository;
    @Spy
    private  DtoToEntityMapper dtoToEntityMapper;

    @InjectMocks
    private ExcelFileHandler fileHandler;
    private int classNum=1;
    private Section section;
    @BeforeEach
    void setUp() {
         section= Section.builder()
                .id(UUID.randomUUID().toString())
                .name("Section 5")
                .createdAt(LocalDateTime.now())
                .sectionId(5)
                .geologicalClasses(
                        Collections.singleton(GeologicalClass
                                .builder()
                                .status(Status.ACTIVE)
                                .id(UUID.randomUUID().toString())
                                .code("GL5"+classNum)
                                .name("gl-class 5"+classNum)
                                .classNumber(classNum)
                                .createdAt(LocalDateTime.now())
                                .build())
                ).build();
    }
    private InputStream getResourceAsStream(String resourcePath) throws IOException {
        return new FileInputStream(Objects.requireNonNull(getClass().getClassLoader().getResource("file/"+resourcePath)).getFile());
    }
    @Test
    void test_importFile_Success() throws Exception {
        InputStream resourceAsStream = getResourceAsStream("geo-classify_2024-07-19_40077428702772.xlsx");
        List<Section> sections = fileHandler.importFile(resourceAsStream);

        assertNotNull(sections);
        assertEquals(1 , sections.size());
        assertNotNull(sections.getFirst().getGeologicalClasses());
        assertEquals(2 , sections.getFirst().getGeologicalClasses().size());
    }

    @Test
    void test_exportFile_Success() throws Exception {
        byte[] fileContent = fileHandler.exportFile(Collections.singletonList(section));
        assertNotNull(fileContent);
        try (XSSFWorkbook workbook = new XSSFWorkbook(new ByteArrayInputStream(fileContent))) {
            assertEquals(1, workbook.getNumberOfSheets());
            assertEquals("Sections", workbook.getSheetAt(0).getSheetName());

            XSSFSheet sheetAt = workbook.getSheetAt(0);
            XSSFRow row = sheetAt.getRow(1);
            XSSFCell codeCell = row.getCell(classNum * 2);
            XSSFCell nameCell = row.getCell((classNum * 2)-1);

            assertNotNull(codeCell);
            assertNotNull(nameCell);
            assertEquals("GL5"+classNum,codeCell.getStringCellValue());
            assertEquals("gl-class 5"+classNum,nameCell.getStringCellValue());

        }
    }

    @Test
    void test_exportFile_Failed_maxExcelColumnNumber() throws Exception {
        classNum = 16385;
        GeologicalClass build = GeologicalClass
                .builder()
                .status(Status.ACTIVE)
                .id(UUID.randomUUID().toString())
                .code("GL5" + classNum)
                .name("gl-class 5" + classNum)
                .classNumber(classNum)
                .createdAt(LocalDateTime.now())
                .build();
        section.setGeologicalClasses(Collections.singleton(build));

        ValidationFailedException ex = assertThrows(ValidationFailedException.class, () ->
                fileHandler.exportFile(Collections.singletonList(section)));
        assertEquals("Export Failed - Error detected while calculating the max column count for geologicalClasses",ex.getLocalizedMessage());
    }

    @Test
    void test_getFileExtension_Success() {
        assertEquals("xlsx",fileHandler.getFileExtension());
    }
}