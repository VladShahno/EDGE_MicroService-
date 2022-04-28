package com.lenovo.training.edge.entity;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailData {

    @NotBlank(message = "{not.blank}")
    private String subject;

    @NotBlank(message = "{not.blank}")
    private String recipient;

    @NotBlank(message = "{not.blank}")
    private String sender;

    @NotBlank(message = "{not.blank}")
    private String message;
}
