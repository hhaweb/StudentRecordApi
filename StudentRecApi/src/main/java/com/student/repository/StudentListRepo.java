package com.student.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.student.entity.Student;

public interface StudentListRepo extends PagingAndSortingRepository<Student, Long>{


}
