package com.lenovo.training.edge.service.impl;

import com.lenovo.training.edge.dto.DeviceDto;
import com.lenovo.training.edge.dto.FileInfoDto;
import com.lenovo.training.edge.service.AuthService;
import com.lenovo.training.edge.service.DeviceService;
import com.lenovo.training.edge.service.FileInfoService;
import com.lenovo.training.edge.service.ImportCsvFileService;
import com.lenovo.training.edge.service.KafkaProducerService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private ImportCsvFileService importCsvFileService;
    private KafkaProducerService kafkaProducerService;
    private FileInfoService fileInfoService;
    private AuthService authService;

    @Override
    public List<DeviceDto> createFromCsvDevicesAndFileInfo(MultipartFile file) {

        List<DeviceDto> devicesFromCsv = importCsvFileService.readCsvFile(DeviceDto.class, file);

        List<DeviceDto> coreResponseList = kafkaProducerService.send(devicesFromCsv);

        if (!coreResponseList.isEmpty()) {
            fileInfoService.saveFileInfoWithEmailSending(
                new FileInfoDto(authService.getToken().getPreferredUsername(),
                    file.getOriginalFilename(), coreResponseList.size()));
        }
        return coreResponseList;
    }
}
