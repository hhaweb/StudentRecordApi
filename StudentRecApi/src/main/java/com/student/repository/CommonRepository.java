package com.student.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.student.dto.common.SelectedItem;
import com.student.entity.DropDownItem;
import com.student.entity.Student;

public interface CommonRepository extends JpaRepository<DropDownItem, Long>{
	
	@Query(value = "select distinct s.status from Student s")
	List<String> getStudentStatus();
	
	@Query(value = "select distinct c.courseLevel from Course c")
	List<String> getCourseLevel();
	
	List<DropDownItem> findByName(String name);
	
	@Transactional
	Long deleteByName(String name);
	
	@Query(value = "select distinct d.name from DropDownItem d")
	List<String> getDropDownName();
}
