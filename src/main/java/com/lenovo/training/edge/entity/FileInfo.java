package com.lenovo.training.edge.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "files")
public class FileInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{not.blank}")
    @Column(name = "user_name")
    private String user;

    @NotBlank(message = "{not.blank}")
    @Column(name = "file_name")
    private String fileName;

    @Column(name = "amount")
    private int amount;

    @Column(name = "download_date")
    private Date downloadDate;

    public FileInfo(String user, String fileName, int amount) {
        this.user = user;
        this.fileName = fileName;
        this.amount = amount;
        this.downloadDate = new Date();
    }
}