package com.student.service;

import java.text.ParseException;
import java.util.List;

import com.student.dto.UserDto;
import com.student.dto.common.GenericResponse;
import com.student.entity.RoutePermission;

public interface UserService {
	GenericResponse createUser(UserDto userDto) throws ParseException;
	List<RoutePermission> getRoutePermissionByRoleNameList(List<String> roleNameList);
}
