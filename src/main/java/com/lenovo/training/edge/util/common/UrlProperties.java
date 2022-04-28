package com.lenovo.training.edge.util.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("url")
@Data
public class UrlProperties {

    private String base;
    private String series;
    private String all;
    private String model;
}
