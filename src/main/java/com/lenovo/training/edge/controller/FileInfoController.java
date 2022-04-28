package com.lenovo.training.edge.controller;

import com.lenovo.training.edge.entity.FileInfo;
import com.lenovo.training.edge.service.FileInfoService;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("files")
public class FileInfoController {

    private FileInfoService fileInfoService;

    @GetMapping(value = "/uploaded")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Returns information about all previously uploaded files.")
    public List<FileInfo> getAllUploadedFiles() {
        return fileInfoService.getAllDownloadedFiles();
    }
}
