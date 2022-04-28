package com.lenovo.training.edge.repository;

import com.lenovo.training.edge.entity.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {
}
