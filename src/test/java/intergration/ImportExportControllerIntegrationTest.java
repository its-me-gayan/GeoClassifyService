package intergration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.natlex.geo.GeoClassifyServiceRunner;
import org.natlex.geo.entity.ExportJob;
import org.natlex.geo.helper.impl.EntityToDtoMapper;
import org.natlex.geo.helper.impl.ResponseGenerator;
import org.natlex.geo.repository.IExportJobRepository;
import org.natlex.geo.repository.IImportJobRepository;
import org.natlex.geo.service.IExportImportAsyncService;
import org.natlex.geo.service.IExportImportService;
import org.natlex.geo.util.JobStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import utils.TestAuthUtil;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/21/24
 * Time: 1:12 PM
 */

@SpringBootTest(classes = GeoClassifyServiceRunner.class)
@AutoConfigureMockMvc
public class ImportExportControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IImportJobRepository importJobRepository;

    @MockBean
    private IExportJobRepository exportJobRepository;

    @Spy
    private IExportImportAsyncService importExportAsyncService;

    @Spy
    private ResponseGenerator responseGenerator;

    @Spy
    private EntityToDtoMapper entityToDtoMapper;

    @Autowired
    private IExportImportService importExportService;

    @Test
    void test_DownloadExportedFile_200_Success() throws Exception {


        ExportJob exportJob = ExportJob.builder()
                .id(1L)
                .completedAt(LocalDateTime.now())
                .jobCompletedMessage("File Export success and xlsx file saved")
                .status(JobStatus.DONE)
                .createdAt(LocalDateTime.now())
                .file(new byte[]{116, 101, 115, 116})
                .fileName("test.xlsx")
                .build();

        Mockito.when(exportJobRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(exportJob));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/export/1/file")
                        .headers(TestAuthUtil.getAuthHeader()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Disposition", "attachment;filename=test.xlsx"))
                .andReturn();

        byte[] responseBytes = mvcResult.getResponse().getContentAsByteArray();
        Assertions.assertEquals("test" , new String(responseBytes));
    }
}
