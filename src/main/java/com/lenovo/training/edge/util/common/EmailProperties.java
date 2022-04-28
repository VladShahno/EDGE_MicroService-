package com.lenovo.training.edge.util.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("email")
@Data
public class EmailProperties {

    private String subject;
    private String recipient;
    private String sender;
    private String message;
}
