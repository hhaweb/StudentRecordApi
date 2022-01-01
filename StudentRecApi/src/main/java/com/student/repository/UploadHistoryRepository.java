package com.student.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.student.entity.UploadHistory;

public interface UploadHistoryRepository extends JpaRepository<UploadHistory, Long>{
	
	@Query(nativeQuery =true,value = "select * from upload_history order by upload_date desc")
	List<UploadHistory> getAllUploadHistory();
}
