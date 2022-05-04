package com.lenovo.training.edge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lenovo.training.edge.DemoApplication;
import com.lenovo.training.edge.dto.DeviceDto;
import com.lenovo.training.edge.payload.response.ExceptionResponse;
import com.lenovo.training.edge.repository.FileInfoRepository;
import java.io.IOException;
import java.util.List;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.http.HttpHeaders;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.reactive.function.client.WebClient;

import static com.lenovo.training.edge.util.common.Constant.Headers.BEARER;
import static com.lenovo.training.edge.util.common.TestConstant.ContentType.APPLICATION_JSON;
import static com.lenovo.training.edge.util.common.TestConstant.ContentType.CONTENT_TYPE;
import static com.lenovo.training.edge.util.common.TestConstant.Uri.BASE_URL;
import static com.lenovo.training.edge.util.common.TestConstant.Uri.MODEL;
import static com.lenovo.training.edge.util.common.TestConstant.Uri.SERIES;
import static com.lenovo.training.edge.util.common.TestConstant.WRONG_RESOURCE;
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
@ContextConfiguration(classes = {DemoApplication.class, DeviceControllerTest.TestConfig.class})
public class DeviceControllerTest {

    public static MockWebServer mockServer;
    private final ObjectMapper objectMapper = new ObjectMapper();
    DeviceDto deviceDto = new DeviceDto("C3611", "Z2XQ",
        "ThinkStation P620");
    @Autowired
    private FileInfoRepository fileInfoRepository;
    @Autowired
    private MockMvc mockMvc;

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
    @DisplayName("Get request to /devices/series should get a device specified by \"serialNumber\"")
    void getBySerialNumberRequestShouldReturnDevice() throws Exception {

        String responseBody = objectMapper.writeValueAsString(deviceDto);
        mockEndpointResponse(HttpStatus.OK, responseBody);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
            .get(BASE_URL + SERIES + deviceDto.getSerialNumber()));

        verifyResult(resultActions, HttpStatus.OK, responseBody);
    }

    @Test
    @DisplayName("Get request to /devices/model should get all devices specified by \"model\"")
    void getAllDevicesByModelRequestShouldReturnDevices() throws Exception {

        String responseBody = objectMapper.writeValueAsString(List.of(deviceDto,
            new DeviceDto("S2", "Z2XQ", "ZTE2 Phone")));
        mockEndpointResponse(HttpStatus.OK, responseBody);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(BASE_URL + MODEL + deviceDto.getModel()))
            .andExpect(jsonPath("$[0].model").value(deviceDto.getModel()))
            .andExpect(jsonPath("$[1].model").value(deviceDto.getModel()));

        verifyResult(resultActions, HttpStatus.OK, responseBody);
    }

    @Test
    @DisplayName("Get request to /devices/model should return an empty array if model not exists")
    void getAllDevicesByModelRequestShouldReturnEmptyArray() throws Exception {

        String responseBody = objectMapper.writeValueAsString(List.of());
        mockEndpointResponse(HttpStatus.OK, responseBody);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
            .get(BASE_URL + MODEL + WRONG_RESOURCE));

        verifyResult(resultActions, HttpStatus.OK, responseBody);
    }

    @Test
    @DisplayName("Get request to /devices/series should return Not Found status")
    void getBySerialNumberRequestShouldReturnNotFoundStatus() throws Exception {

        ExceptionResponse serialNumberExistsResponse = ExceptionResponse
            .builder()
            .status(HttpStatus.NOT_FOUND.toString())
            .build();

        String responseBody = objectMapper.writeValueAsString(serialNumberExistsResponse);
        MockResponse mockResponse = new MockResponse().setResponseCode(HttpStatus.NOT_FOUND.value())
            .setBody(responseBody)
            .addHeader(CONTENT_TYPE, APPLICATION_JSON);
        mockServer.enqueue(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders
                .get(BASE_URL + SERIES + WRONG_RESOURCE)).andDo(print())
            .andExpect(status().is(HttpStatus.NOT_FOUND.value()));

    }

    private void mockEndpointResponse(HttpStatus httpStatus, String body) {
        MockResponse mockResponse = new MockResponse().setResponseCode(httpStatus.value())
            .setBody(body)
            .addHeader(CONTENT_TYPE, APPLICATION_JSON)
            .addHeader(HttpHeaders.AUTHORIZATION, BEARER);
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
}
