package com.lenovo.training.edge.service;

import com.lenovo.training.edge.dto.DeviceDto;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface DeviceService {

    List<DeviceDto> createFromCsvDevicesAndFileInfo(MultipartFile file);
}
