package com.lenovo.training.edge.service.impl;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.lenovo.training.edge.exception.ImportCsvException;
import com.lenovo.training.edge.exception.ResourceNotFoundException;
import com.lenovo.training.edge.service.ImportCsvFileService;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static com.lenovo.training.edge.util.common.Constant.ExceptionMessage.NO_FILE_PRESENT;
import static com.lenovo.training.edge.util.common.Constant.ExceptionMessage.UNEXPECTED_IMPORT_ERROR;

@Service
@AllArgsConstructor
public class ImportCsvFileServiceImpl implements ImportCsvFileService {

    private static final Logger LOGGER = LoggerFactory.getLogger("ImportCsvFileServiceImpl");

    @Override
    public <T> List<T> readCsvFile(Class<T> clazz, MultipartFile file) {
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper
            .schemaFor(clazz)
            .withHeader()
            .withColumnReordering(true);
        ObjectReader reader = mapper.readerFor(clazz).with(schema);
        try {
            if (Objects.isNull(file)) {
                throw new ResourceNotFoundException(NO_FILE_PRESENT);
            }
            LOGGER.info("Reading file - " + file.getOriginalFilename());
            return reader.<T>readValues(file.getInputStream()).readAll();
        } catch (IOException e) {
            LOGGER.error("Error reading file!");
            throw new ImportCsvException(UNEXPECTED_IMPORT_ERROR);
        }
    }
}
