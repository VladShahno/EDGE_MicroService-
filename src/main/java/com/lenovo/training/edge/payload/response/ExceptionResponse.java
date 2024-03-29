package com.lenovo.training.edge.payload.response;

import java.time.OffsetDateTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {

    private String status;

    private String error;

    private String message;

    private String path;

    private OffsetDateTime timestamp;

    private Map<String, Object> details;
}
