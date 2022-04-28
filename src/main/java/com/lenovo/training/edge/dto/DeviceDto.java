package com.lenovo.training.edge.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.lenovo.training.edge.util.common.Constant.RegularExpressions.MODEL_REGEXP;
import static com.lenovo.training.edge.util.common.Constant.RegularExpressions.SERIAL_NUMBER_REGEXP;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDto {

    @NotBlank(message = "{not.blank}")
    @Pattern(regexp = SERIAL_NUMBER_REGEXP, message = "{bad.serial.number}")
    private String serialNumber;

    @NotBlank(message = "{not.blank}")
    @Pattern(regexp = MODEL_REGEXP, message = "{bad.model}")
    private String model;

    @NotBlank(message = "{not.blank}")
    private String description;
}
