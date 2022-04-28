package com.lenovo.training.edge.mapper;

import com.lenovo.training.edge.dto.FileInfoDto;
import com.lenovo.training.edge.entity.FileInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileInfoMapper {

    FileInfo mapFileInfoDtoToFileInfo(FileInfoDto fileInfoDto);

    FileInfoDto mapFileInfoToFileInfoDto(FileInfoDto fileInfoDto);
}
