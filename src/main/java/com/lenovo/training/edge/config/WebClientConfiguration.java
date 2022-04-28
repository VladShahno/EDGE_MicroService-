package com.lenovo.training.edge.config;

import com.lenovo.training.edge.util.common.UrlProperties;
import io.netty.handler.logging.LogLevel;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;


@Configuration
@AllArgsConstructor
public class WebClientConfiguration {

    private final HttpClient httpClient = HttpClient.create()
        .wiretap(this.getClass().getCanonicalName(), LogLevel.INFO, AdvancedByteBufFormat.TEXTUAL);
    private final ClientHttpConnector conn = new ReactorClientHttpConnector(httpClient);
    private UrlProperties urlProperties;

    @Bean
    public WebClient getWebClient() {
        return WebClient.builder()
            .clientConnector(conn)
            .baseUrl(urlProperties.getBase())
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }
}