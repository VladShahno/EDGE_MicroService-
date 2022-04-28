package com.lenovo.training.edge.service.impl;

import com.lenovo.training.edge.dto.FileInfoDto;
import com.lenovo.training.edge.entity.EmailData;
import com.lenovo.training.edge.entity.FileInfo;
import com.lenovo.training.edge.mapper.FileInfoMapper;
import com.lenovo.training.edge.repository.FileInfoRepository;
import com.lenovo.training.edge.service.FileInfoService;
import com.lenovo.training.edge.service.MailService;
import com.lenovo.training.edge.util.common.EmailProperties;
import java.util.List;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FileInfoServiceImpl implements FileInfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger("FileInfoServiceImpl");
    private final FileInfoRepository fileInfoRepository;
    private final MailService mailService;
    private final EmailProperties emailProperties;
    private final FileInfoMapper fileInfoMapper;

    @Override
    public List<FileInfo> getAllDownloadedFiles() {
        LOGGER.info("Getting all downloaded files");
        return fileInfoRepository.findAll();
    }

    @Override
    public void saveFileInfoWithEmailSending(FileInfoDto fileInfoDto) {

        saveFileInfo(fileInfoDto);

        LOGGER.info("Sending email...");
        mailService.sendEmail(
            new EmailData(emailProperties.getSubject(), emailProperties.getRecipient(),
                emailProperties.getSender(),
                emailProperties.getMessage() + fileInfoDto.getFileName()));
    }

    private void saveFileInfo(FileInfoDto fileInfoDto) {
        LOGGER.info("Creating FileInfo with file name - " + fileInfoDto.getFileName());
        fileInfoRepository.save(fileInfoMapper.mapFileInfoDtoToFileInfo(fileInfoDto));
    }
}