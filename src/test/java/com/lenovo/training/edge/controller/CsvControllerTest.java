/*
package com.lenovo.training.edge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.lenovo.training.edge.DemoApplication;
import com.lenovo.training.edge.dto.DeviceDto;
import com.lenovo.training.edge.payload.response.ExceptionResponse;
import com.lenovo.training.edge.repository.FileInfoRepository;
import com.lenovo.training.edge.service.AuthService;
import com.lenovo.training.edge.util.common.EmailProperties;
import java.io.IOException;
import java.util.List;
import javax.mail.internet.MimeMessage;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.keycloak.representations.AccessToken;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.reactive.function.client.WebClient;

import static com.lenovo.training.edge.util.common.Constant.ExceptionMessage.UNEXPECTED_IMPORT_ERROR;
import static com.lenovo.training.edge.util.common.TestConstant.BAD_FILE_CONTENT;
import static com.lenovo.training.edge.util.common.TestConstant.ContentType.APPLICATION_JSON;
import static com.lenovo.training.edge.util.common.TestConstant.ContentType.CONTENT_TYPE;
import static com.lenovo.training.edge.util.common.TestConstant.FILE_CONTENT;
import static com.lenovo.training.edge.util.common.TestConstant.FILE_CONTENT_DUPLICATION;
import static com.lenovo.training.edge.util.common.TestConstant.FILE_NAME;
import static com.lenovo.training.edge.util.common.TestConstant.ORIGINAL_FILE_NAME;
import static com.lenovo.training.edge.util.common.TestConstant.RESPONSE_MESSAGE;
import static com.lenovo.training.edge.util.common.TestConstant.RESPONSE_MESSAGE_CSV;
import static com.lenovo.training.edge.util.common.TestConstant.SERIAL_NUMBER_ALREADY_EXISTS;
import static com.lenovo.training.edge.util.common.TestConstant.TEST_USER;
import static com.lenovo.training.edge.util.common.TestConstant.Uri.BASE_URL;
import static com.lenovo.training.edge.util.common.TestConstant.Uri.CSV_MODEL;
import static com.lenovo.training.edge.util.common.TestConstant.Uri.UPLOAD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@SpringBootTest
@TestPropertySource(properties = {
    "spring.main.allow-bean-definition-overriding=true"},
    locations = "classpath:application-test.properties")
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {DemoApplication.class, CsvControllerTest.TestConfig.class})
public class CsvControllerTest {

    public static MockWebServer mockServer;
    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
        .withConfiguration(
            GreenMailConfiguration.aConfig()
                .withUser("shakhno2022@gmail.com", "qWERTY2017!"))
        .withPerMethodLifecycle(true);
    private final ObjectMapper objectMapper = new ObjectMapper();
    DeviceDto deviceDto = new DeviceDto("C3611", "Z2XQ",
        "ThinkStation P620");
    @Autowired
    private FileInfoRepository fileInfoRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmailProperties emailProperties;
    @Autowired
    public AuthService authService;
    @Autowired
    public AccessToken accessToken;

    @BeforeAll
    static void beforeAll() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();
    }

    @AfterAll
    static void afterAll() throws IOException {
        mockServer.shutdown();
    }

    @AfterEach
    void afterEach() {
        Mockito.when(accessToken.getPreferredUsername()).thenReturn(TEST_USER);
        Mockito.when(authService.getToken()).thenReturn(accessToken);
        fileInfoRepository.deleteAll();
    }

    @TestConfiguration
    public static class TestConfig {
        @Bean(name = "getWebClient")
        WebClient testWebClient() {
            HttpUrl url = mockServer.url("/");
            return WebClient.create(url.toString());
        }
    }

    @Test
    @DisplayName("Post request to csv/upload should import csv file to MongoDB in CORE "
        + "microservice and save FileInfo to" + " Postgres in EDGE microservice")
    void postCsvRequestShouldImportDataFromCsvFileAndSaveFileInfo() throws Exception {

        String responseBody = objectMapper.writeValueAsString(deviceDto);

        mockEndpointResponse(HttpStatus.CREATED, responseBody);
        MockMultipartFile file = new MockMultipartFile(FILE_NAME, ORIGINAL_FILE_NAME,
            APPLICATION_JSON, FILE_CONTENT.getBytes());
        ResultActions resultActions =
            mockMvc.perform(MockMvcRequestBuilders.multipart(BASE_URL + UPLOAD)
                .file(file));
        MimeMessage[] receivedMessage = greenMail.getReceivedMessages();

        verifyResult(resultActions, HttpStatus.CREATED, RESPONSE_MESSAGE);
        assertEquals(1, fileInfoRepository.findAll().size());
        assertEquals(receivedMessage[0].getSubject(), emailProperties.getSubject());
    }

    @Test
    @DisplayName("Post request to csv/upload should Thrown ResourceExistsException")
    void postCsvRequestWithExistingSerialNumberShouldThrownResourceExistsException()
        throws Exception {

        ExceptionResponse serialNumberExistsResponse = ExceptionResponse
            .builder()
            .status(HttpStatus.BAD_REQUEST.toString())
            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
            .message(SERIAL_NUMBER_ALREADY_EXISTS)
            .build();

        String responseBody = objectMapper.writeValueAsString(serialNumberExistsResponse);
        mockEndpointResponse(HttpStatus.BAD_REQUEST, responseBody);
        MockMultipartFile file = new MockMultipartFile(FILE_NAME, ORIGINAL_FILE_NAME,
            APPLICATION_JSON, FILE_CONTENT.getBytes());
        MimeMessage[] receivedMessage = greenMail.getReceivedMessages();


        mockMvc.perform(MockMvcRequestBuilders.multipart(BASE_URL + UPLOAD)
                .file(file))
            .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
            .andExpect(jsonPath("$.message").value(SERIAL_NUMBER_ALREADY_EXISTS));
        assertEquals(0, fileInfoRepository.findAll().size());
        assertEquals(receivedMessage.length, 0);
    }

    @Test
    @DisplayName("Get request to /devices/csv/ should get all devices specified by \"model\" "
        + "in csv format")
    void getAllDevicesByModelRequestShouldReturnDevicesInCsvFormat() throws Exception {

        String responseBody = objectMapper.writeValueAsString(List.of(deviceDto,
            new DeviceDto("S2", "Z2XQ", "ZTE2 Phone")));
        mockEndpointResponse(HttpStatus.OK, responseBody);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
            .get(BASE_URL + CSV_MODEL + deviceDto.getModel()));

        verifyResult(resultActions, HttpStatus.OK, RESPONSE_MESSAGE_CSV);
    }

    private void mockEndpointResponse(HttpStatus httpStatus, String body) {
        MockResponse mockResponse = new MockResponse().setResponseCode(httpStatus.value())
            .setBody(body)
            .addHeader(CONTENT_TYPE, APPLICATION_JSON);
        mockServer.enqueue(mockResponse);
    }

    private void verifyResult(ResultActions resultActions, HttpStatus httpStatus, String... message)
        throws Exception {
        resultActions
            .andDo(print())
            .andExpect(status().is(httpStatus.value()));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        Assertions.assertThat(responseBody).contains(message);
    }

    @Nested
    class postCsvTestWithAnotherMockResponse {
        @Test
        @DisplayName("Post request to csv/upload should Throw ImportCsvException")
        void postCsvRequestWithBadFileContentShouldThrowImportCsvException() throws Exception {

            ExceptionResponse exceptionResponse = ExceptionResponse
                .builder()
                .status(HttpStatus.BAD_REQUEST.toString())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(UNEXPECTED_IMPORT_ERROR)
                .build();
            String responseBody = objectMapper.writeValueAsString(exceptionResponse);

            mockEndpointResponse(HttpStatus.BAD_REQUEST, responseBody);
            MockMultipartFile file = new MockMultipartFile(FILE_NAME, ORIGINAL_FILE_NAME,
                APPLICATION_JSON, BAD_FILE_CONTENT.getBytes());
            MimeMessage[] receivedMessage = greenMail.getReceivedMessages();

            mockMvc.perform(MockMvcRequestBuilders.multipart(BASE_URL + UPLOAD)
                    .file(file))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(UNEXPECTED_IMPORT_ERROR));
            assertEquals(0, fileInfoRepository.findAll().size());
            assertEquals(receivedMessage.length, 0);
        }

        @Test
        @DisplayName("Post request to csv/upload import only unique Devices from csv file to "
            + "MongoDB in CORE microservice and save FileInfo to Postgres in EDGE microservice")
        void postCsvRequestWithDuplicateSerialNumberShouldSaveOnlyFirstUnique() throws Exception {

            String responseBody = objectMapper.writeValueAsString(deviceDto);

            mockEndpointResponse(HttpStatus.CREATED, responseBody);
            MockMultipartFile file = new MockMultipartFile(FILE_NAME, ORIGINAL_FILE_NAME,
                APPLICATION_JSON, FILE_CONTENT_DUPLICATION.getBytes());
            ResultActions resultActions =
                mockMvc.perform(MockMvcRequestBuilders.multipart(BASE_URL + UPLOAD)
                    .file(file));
            MimeMessage[] receivedMessage = greenMail.getReceivedMessages();

            verifyResult(resultActions, HttpStatus.CREATED, RESPONSE_MESSAGE);
            assertEquals(1, fileInfoRepository.findAll().size());
            assertEquals(receivedMessage[0].getSubject(), emailProperties.getSubject());
        }
    }
}
*/
