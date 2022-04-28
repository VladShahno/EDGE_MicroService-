package com.lenovo.training.edge.service.impl;

import com.lenovo.training.edge.dto.DeviceDto;
import com.lenovo.training.edge.exception.ResourceExistsException;
import com.lenovo.training.edge.exception.ResourceNotFoundException;
import com.lenovo.training.edge.payload.response.ExceptionResponse;
import com.lenovo.training.edge.service.AuthService;
import com.lenovo.training.edge.service.WebClientCoreService;
import com.lenovo.training.edge.util.common.UrlProperties;
import java.util.List;
import lombok.AllArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.lenovo.training.edge.util.common.Constant.Headers.BEARER;

@Service
@AllArgsConstructor
public class WebClientCoreServiceImpl implements WebClientCoreService {

    private final WebClient webClient;
    private UrlProperties urlProperties;
    private AuthService authService;

    @Override
    public List<DeviceDto> getDevicesByModel(String model) {
        return webClient
            .get()
            .uri(urlProperties.getModel() + model)
            .header(HttpHeaders.AUTHORIZATION, BEARER
                + authService.getSecurityContext().getTokenString())
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError, clientResponse ->
                clientResponse.bodyToMono(ExceptionResponse.class)
                    .flatMap(error ->
                        Mono.error(new ResourceNotFoundException(error.getMessage()))))
            .bodyToFlux(DeviceDto.class)
            .collectList()
            .block();
    }

    @Override
    public DeviceDto getDeviceBySerialNumber(String serialNumber) {
        return webClient
            .get()
            .uri(urlProperties.getSeries() + serialNumber)
            .header(HttpHeaders.AUTHORIZATION, BEARER
                + authService.getSecurityContext().getTokenString())
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError, clientResponse ->
                clientResponse.bodyToMono(ExceptionResponse.class)
                    .flatMap(error ->
                        Mono.error(new ResourceNotFoundException(error.getMessage()))))
            .bodyToMono(DeviceDto.class)
            .block();
    }

    @Override
    public List<DeviceDto> createDevices(List<DeviceDto> deviceDtoList) {
        return webClient
            .post()
            .uri(urlProperties.getAll())
            .header(HttpHeaders.AUTHORIZATION, BEARER
                + authService.getSecurityContext().getTokenString())
            .body(Mono.just(deviceDtoList), DeviceDto.class)
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError, clientResponse ->
                clientResponse.bodyToMono(ExceptionResponse.class)
                    .flatMap(error ->
                        Mono.error(new ResourceExistsException(error.getMessage()))))
            .bodyToFlux(DeviceDto.class)
            .collectList()
            .block();
    }
}