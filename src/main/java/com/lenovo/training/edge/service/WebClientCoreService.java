package com.lenovo.training.edge.service;

import com.lenovo.training.edge.dto.DeviceDto;
import java.util.List;

public interface WebClientCoreService {

    List<DeviceDto> getDevicesByModel(String model);

    DeviceDto getDeviceBySerialNumber(String serialNumber);
}
