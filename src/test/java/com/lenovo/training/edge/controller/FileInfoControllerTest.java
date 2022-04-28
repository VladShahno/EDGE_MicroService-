package com.lenovo.training.edge.controller;

import com.lenovo.training.edge.entity.FileInfo;
import com.lenovo.training.edge.repository.FileInfoRepository;
import com.lenovo.training.edge.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.lenovo.training.edge.util.common.TestConstant.ORIGINAL_FILE_NAME;
import static com.lenovo.training.edge.util.common.TestConstant.TEST_USER;
import static com.lenovo.training.edge.util.common.TestConstant.Uri.BASE_URL;
import static com.lenovo.training.edge.util.common.TestConstant.Uri.UPLOADED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
public class FileInfoControllerTest {

    @Autowired
    private FileInfoRepository fileInfoRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    public AuthService authService;
    @Autowired
    public AccessToken accessToken;

    @Test
    @DisplayName("GET request to \"/uploaded\" endpoint should returns information about all "
        + "previously uploaded files")
    void getAllDownLoadedFilesShouldReturnInformationAboutAllPreviouslyUploaded() throws Exception {
        fileInfoRepository.save(new FileInfo(TEST_USER, ORIGINAL_FILE_NAME, 1));
        fileInfoRepository.save(new FileInfo(TEST_USER, ORIGINAL_FILE_NAME, 3));

        getResponse(HttpMethod.GET, BASE_URL + UPLOADED, "", HttpStatus.OK)
            .andExpect(jsonPath("$[0].fileName").value(ORIGINAL_FILE_NAME))
            .andExpect(jsonPath("$[0].amount").value(1))
            .andExpect(jsonPath("$[1].fileName").value(ORIGINAL_FILE_NAME))
            .andExpect(jsonPath("$[1].amount").value(3));

        assertEquals(2, fileInfoRepository.findAll().size());
    }

    @Test
    @DisplayName("GET request to \"/uploaded\" endpoint should returns an empty array if no "
        + "FileInfo present")
    void getAllDevicesByModelRequestShouldReturnEmptyArray() throws Exception {

        getResponse(HttpMethod.GET, BASE_URL + UPLOADED, "", HttpStatus.OK);

        assertEquals(0, fileInfoRepository.findAll().size());
    }


    private ResultActions getResponse(HttpMethod method, String endpoint, String contentData,
                                      HttpStatus httpStatus) throws Exception {

        return mockMvc.perform(MockMvcRequestBuilders
                .request(HttpMethod.valueOf(method.name()), endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentData))
            .andExpect(status().is(httpStatus.value()));
    }
}
