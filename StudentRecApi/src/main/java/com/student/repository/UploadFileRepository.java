package com.student.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.student.entity.UploadFileRecord;

public interface UploadFileRepository extends JpaRepository<UploadFileRecord, Long>{

}
