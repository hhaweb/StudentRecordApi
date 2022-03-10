package com.student.service;

import java.text.ParseException;
import java.util.List;

import com.student.dto.UserDetailsImpl;
import com.student.dto.UserDto;
import com.student.dto.common.GenericResponse;
import com.student.dto.common.SearchDto;
import com.student.entity.Role;
import com.student.entity.RoutePermission;

public interface UserService {
	GenericResponse createUser(UserDto userDto) throws ParseException;
	GenericResponse deleteUser(Long userId);
	GenericResponse changePasswordForFirstTimeLogin(UserDto userDto);
	List<RoutePermission> getRoutePermissionByRoleNameList(List<String> roleNameList);
	List<UserDto> getUserListWithPager(SearchDto searchDto);
	UserDto getUserById(Long id);
	List<Role> getRole();
	UserDetailsImpl findByUserNameAndPlainPassword(String userName, String password);
}
