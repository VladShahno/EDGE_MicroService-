package com.lenovo.training.edge.controller;

import com.lenovo.training.edge.dto.DeviceDto;
import com.lenovo.training.edge.service.DeviceService;
import com.lenovo.training.edge.service.WebClientCoreService;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static com.lenovo.training.edge.util.common.Constant.ContentType.APPLICATION_CSV;
import static com.lenovo.training.edge.util.common.Constant.ContentType.MULTIPART_FORM_DATA;

@RestController
@AllArgsConstructor
@RequestMapping("devices/csv")
public class CsvController {

    private DeviceService deviceService;
    private WebClientCoreService webClientCoreService;

    @PostMapping(value = "/upload", consumes = MULTIPART_FORM_DATA)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Create devices from csv file in CORE microservice. Also create information "
        + "about saved devices in EDGE microservice and send email about it. Throws an exception "
        + "if the serial number(s) in CORE microservice already exists")
    public List<DeviceDto> createDevicesFromCsv(@RequestParam(value = "file") MultipartFile file) {
        return deviceService.createFromCsvDevicesAndFileInfo(file);
    }

    @GetMapping(value = "/model/{model}", produces = APPLICATION_CSV)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
        "Returns a Device by model in csv format. Returns an empty array if the requested model "
            + "does not exist.")
    public List<DeviceDto> getAllDevicesByModelCsv(
        @PathVariable @NotBlank(message = "{not.blank}") String model) {
        return webClientCoreService.getDevicesByModel(model);
    }
}