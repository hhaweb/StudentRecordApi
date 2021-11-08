package com.student.service.user;

import java.text.ParseException;
import java.util.List;

import com.student.dto.common.GenericResponse;
import com.student.dto.user.UserDto;
import com.student.entity.user.RoutePermission;

public interface UserService {
	GenericResponse createUser(UserDto userDto) throws ParseException;
	List<RoutePermission> getRoutePermissionByRoleNameList(List<String> roleNameList);
}
