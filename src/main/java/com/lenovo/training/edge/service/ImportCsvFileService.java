package com.lenovo.training.edge.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ImportCsvFileService {

    <T> List<T> readCsvFile(Class<T> clazz, MultipartFile file);
}
