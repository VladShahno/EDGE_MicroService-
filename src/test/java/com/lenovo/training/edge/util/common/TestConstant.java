package com.lenovo.training.edge.util.common;

public class TestConstant {

    public static final String TEST_USER = "testUser";

    public static final String WRONG_RESOURCE = "wrongResource";
    public static final String SERIAL_NUMBER = "serialNumber";
    public static final String MODEL = "model";
    public static final String DESCRIPTION = "description";
    public static final String SERIAL_NUMBER_VALUE = "C3611";
    public static final String MODEL_VALUE = "Z2XQ";
    public static final String DESCRIPTION_VALUE = "ThinkStation P620";
    public static final String LINE_SEPARATOR = System.lineSeparator();
    public static final String COMMA = ",";
    public static final String FILE_NAME = "file";
    public static final String ORIGINAL_FILE_NAME = "test.csv";
    public static final String SERIAL_NUMBER_ALREADY_EXISTS =
        "'Serial number(s) [C3611]' already exists";
    public static final String IMPORT_PATH = "/devices/import/upload/";
    public static final String RESPONSE_MESSAGE = "[{\"serialNumber\":\"C3611\",\"model\":\"Z2XQ\","
        + "\"description\":\"ThinkStation P620\"}]";
    public static final String RESPONSE_MESSAGE_CSV = "serialNumber,model,description\n"
        + "C3611,Z2XQ,\"ThinkStation P620\"\n" + "S2,Z2XQ,\"ZTE2 Phone\"";

    public static final String FILE_CONTENT_DUPLICATION = SERIAL_NUMBER + COMMA + MODEL + COMMA
        + DESCRIPTION + LINE_SEPARATOR + SERIAL_NUMBER_VALUE + COMMA + MODEL_VALUE + COMMA
        + DESCRIPTION_VALUE + LINE_SEPARATOR + SERIAL_NUMBER_VALUE + COMMA + MODEL_VALUE + COMMA
        + DESCRIPTION_VALUE + LINE_SEPARATOR;

    public static final String FILE_CONTENT = SERIAL_NUMBER + COMMA + MODEL + COMMA + DESCRIPTION
        + LINE_SEPARATOR + SERIAL_NUMBER_VALUE + COMMA + MODEL_VALUE + COMMA + DESCRIPTION_VALUE
        + LINE_SEPARATOR;

    public static final String BAD_FILE_CONTENT = SERIAL_NUMBER + MODEL + COMMA + DESCRIPTION
        + LINE_SEPARATOR + SERIAL_NUMBER_VALUE + MODEL_VALUE + DESCRIPTION_VALUE
        + LINE_SEPARATOR;

    public static class ContentType {
        public static final String APPLICATION_JSON = "application/json";
        public static final String CONTENT_TYPE = "Content-Type";
    }

    public static class Uri {
        public static final String BASE_URL = "http://localhost:8081";
        public static final String UPLOAD = "/devices/csv/upload";
        public static final String SERIES = "/devices/series/";
        public static final String UPLOADED = "/files/uploaded";
        public static final String MODEL = "/devices/model/";
        public static final String CSV_MODEL = "/devices/csv/model/";
    }
}
