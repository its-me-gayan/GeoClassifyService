package intergration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.natlex.geo.GeoClassifyServiceRunner;
import org.natlex.geo.dto.SectionRequest;
import org.natlex.geo.dto.generic.GenericRequest;
import org.natlex.geo.entity.Section;
import org.natlex.geo.helper.DtoToEntityMapper;
import org.natlex.geo.helper.EntityToDtoMapper;
import org.natlex.geo.helper.ResponseGenerator;
import org.natlex.geo.repository.SectionRepository;
import org.natlex.geo.service.SectionService;
import org.natlex.geo.util.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import utils.TestAuthUtil;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/20/24
 * Time: 12:14 PM
 */

@SpringBootTest(classes = GeoClassifyServiceRunner.class)
@AutoConfigureMockMvc
class SectionControllerIntegrationTest {
    Section section;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SectionService sectionService;
    @MockBean
    private SectionRepository sectionRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Spy
    private ResponseGenerator responseGenerator;
    @Spy
    private EntityToDtoMapper entityToDtoMapper;
    @Spy
    private DtoToEntityMapper dtoToEntityMapper;

    @BeforeEach
    public void setUp() {
        section = Section.builder()
                .id(UUID.randomUUID().toString())
                .sectionId(10)
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .name("Section 10")
                .build();
    }

    @Test
    void test_AddSection_201_Created() throws Exception {

        SectionRequest sectionRequest = SectionRequest.builder()
                .name("Section 10")
                .build();

        GenericRequest<SectionRequest> sectionRequestGenericRequest = new GenericRequest<>(
                LocalDateTime.now(), "v1", sectionRequest
        );
        Mockito.when(sectionRepository.findSectionBySectionIdAndStatusNot(Mockito.anyInt(), Mockito.any()))
                .thenReturn(Optional.empty());

        Mockito.when(sectionRepository.save(Mockito.any()))
                .thenReturn(section);

        mockMvc.perform(post("/api/v1/section")
                        .headers(TestAuthUtil.getAuthHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sectionRequestGenericRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").isNotEmpty());

    }

    @Test
    void test_AddSection_400_BadRequest_Field_Validation_Failed() throws Exception {
        SectionRequest sectionRequest = SectionRequest.builder()
                .name("")
                .build();

        GenericRequest<SectionRequest> sectionRequestGenericRequest = new GenericRequest<>(
                LocalDateTime.now(), "v1", sectionRequest
        );
        Mockito.when(sectionRepository.findSectionBySectionIdAndStatusNot(Mockito.anyInt(), Mockito.any()))
                .thenReturn(Optional.empty());

        Mockito.when(sectionRepository.save(Mockito.any()))
                .thenReturn(section);

        mockMvc.perform(post("/api/v1/section")
                        .headers(TestAuthUtil.getAuthHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sectionRequestGenericRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldError").isNotEmpty())
                .andExpect(jsonPath("$.fieldError").isArray());
    }
}
