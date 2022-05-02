package com.lenovo.training.edge.service.impl;

import com.lenovo.training.edge.dto.DeviceDto;
import com.lenovo.training.edge.service.KafkaProducerService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KafkaProducerServiceImpl implements KafkaProducerService {

    private final KafkaTemplate<String, List<DeviceDto>> kafkaTemplate;

    @Override
    public List<DeviceDto> send(List<DeviceDto> deviceDtoList) {
        kafkaTemplate.send(kafkaTemplate.getDefaultTopic(), deviceDtoList);
        return deviceDtoList;
    }
}
