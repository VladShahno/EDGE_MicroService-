package com.lenovo.training.edge.service;

import com.lenovo.training.edge.dto.FileInfoDto;
import com.lenovo.training.edge.entity.FileInfo;
import java.util.List;

public interface FileInfoService {

    void saveFileInfoWithEmailSending(FileInfoDto fileInfoDto);

    List<FileInfo> getAllDownloadedFiles();
}
