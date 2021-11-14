package com.student.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.student.entity.RoutePermission;



public interface PermissionRepository extends JpaRepository<RoutePermission, Long>{
	
	@Query(nativeQuery =true,value = "SELECT p.* FROM student_record.route_permission p\r\n" + 
			"LEFT OUTER JOIN student_record.role r\r\n" + 
			"ON r.id = p.role_id WHERE r.name IN (:roleNameList)")
	Optional<List<RoutePermission>> getPermissionByRoleNameList(@Param("roleNameList") List<String> roleNameList);
	

  
}
