package com.lenovo.training.edge.util.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constant {
    public static class ContentType {
        public static final String APPLICATION_CSV = "application/text";
        public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    }

    public static class Headers {
        public static final String BEARER = "Bearer ";
    }

    public static class ExceptionMessage {
        public static final String UNEXPECTED_IMPORT_ERROR = "Unexpected import error";
        public static final String NO_FILE_PRESENT = "No file present";
    }

    public static class RegularExpressions {
        public static final String SERIAL_NUMBER_REGEXP = "^[A-Za-z\\d\\-\\s]+$";
        public static final String MODEL_REGEXP = "^[A-Za-z\\d]+$";
    }
}