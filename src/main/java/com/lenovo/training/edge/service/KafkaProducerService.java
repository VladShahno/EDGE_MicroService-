package com.lenovo.training.edge.service;

import com.lenovo.training.edge.dto.DeviceDto;
import java.util.List;

public interface KafkaProducerService {

    List<DeviceDto> send(List<DeviceDto> deviceDtoList);
}