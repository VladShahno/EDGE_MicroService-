package com.lenovo.training.edge.service.impl;

import com.lenovo.training.edge.dto.DeviceDto;
import com.lenovo.training.edge.dto.FileInfoDto;
import com.lenovo.training.edge.service.DeviceService;
import com.lenovo.training.edge.service.FileInfoService;
import com.lenovo.training.edge.service.ImportCsvFileService;
import com.lenovo.training.edge.service.WebClientCoreService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private ImportCsvFileService importCsvFileService;
    private WebClientCoreService webClientCoreService;
    private FileInfoService fileInfoService;

    @Override
    public List<DeviceDto> createFromCsvDevicesAndFileInfo(MultipartFile file) {

        List<DeviceDto> devicesFromCsv = importCsvFileService.readCsvFile(DeviceDto.class, file);

        List<DeviceDto> coreResponseList = webClientCoreService.createDevices(devicesFromCsv);

        if (!coreResponseList.isEmpty()) {
            fileInfoService.saveFileInfoWithEmailSending(
                new FileInfoDto(file.getOriginalFilename(), coreResponseList.size()));
        }
        return coreResponseList;
    }
}
