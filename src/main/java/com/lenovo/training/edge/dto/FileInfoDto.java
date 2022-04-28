package com.lenovo.training.edge.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileInfoDto {

    private String user;

    private String fileName;

    private int amount;

    private Date downloadDate;

    public FileInfoDto(String user, String fileName, int amount) {
        this.user = user;
        this.fileName = fileName;
        this.amount = amount;
        this.downloadDate = new Date();
    }
}
