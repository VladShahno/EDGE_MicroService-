package com.lenovo.training.edge.service.impl;

import com.lenovo.training.edge.dto.DeviceDto;
import com.lenovo.training.edge.exception.ResourceExistsException;
import com.lenovo.training.edge.exception.ResourceNotFoundException;
import com.lenovo.training.edge.payload.response.ExceptionResponse;
import com.lenovo.training.edge.service.WebClientCoreService;
import com.lenovo.training.edge.util.common.UrlProperties;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class WebClientCoreServiceImpl implements WebClientCoreService {

    private final WebClient webClient;
    private UrlProperties urlProperties;

    @Override
    public List<DeviceDto> getDevicesByModel(String model) {
        return webClient
            .get()
            .uri(urlProperties.getModel() + model)
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