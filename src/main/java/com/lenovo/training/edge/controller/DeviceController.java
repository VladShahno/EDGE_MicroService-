package com.lenovo.training.edge.controller;

import com.lenovo.training.edge.dto.DeviceDto;
import com.lenovo.training.edge.service.WebClientCoreService;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("devices")
@AllArgsConstructor
public class DeviceController {

    private WebClientCoreService webClientCoreService;

    @GetMapping(value = "/model/{model}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Returns a Device by model. Returns an empty array if the requested model does "
        + "not exist.")
    public List<DeviceDto> getAllDevicesByModel(
        @PathVariable @NotBlank(message = "{not.blank}") String model) {
        return webClientCoreService.getDevicesByModel(model);
    }

    @GetMapping(value = "/series/{serialNumber}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Returns a Device by serial number. Throws an exception if the required serial"
        + " number does not exist.")
    public DeviceDto getDeviceBySerialNumber(
        @PathVariable @NotBlank(message = "{not.blank}") String serialNumber) {
        return webClientCoreService.getDeviceBySerialNumber(serialNumber);
    }
}