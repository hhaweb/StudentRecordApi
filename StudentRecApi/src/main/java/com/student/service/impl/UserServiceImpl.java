package com.student.service.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.student.config.ERole;
import com.student.dto.UserDto;
import com.student.dto.common.GenericResponse;
import com.student.dto.common.ResponseMessage;
import com.student.entity.Role;
import com.student.entity.RoutePermission;
import com.student.entity.User;
import com.student.repository.PermissionRepository;
import com.student.repository.RoleRepository;
import com.student.repository.UserRepository;
import com.student.repository.UserRoleRepository;
import com.student.service.UserService;
import com.student.util.CommonUtil;
@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private UserRoleRepository userRoleRepo;
	@Autowired
	private RoleRepository roleRepo;
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	PasswordEncoder encoder;
	@Autowired
	private PermissionRepository permissionRepo;
	
	private GenericResponse updateUser(UserDto userDto) {
		User existingUser = userRepo.findById(userDto.getId()).orElse(null);
		if(existingUser != null) {
			User existingEmail = userRepo.findByUserName(userDto.getUserName()).orElse(null);
			if(existingEmail != null && existingEmail.getId() != userDto.getId()) {
				return new GenericResponse(false, "Error: Email already exist");
			}
			existingUser.setUserName(userDto.getUserName());
			existingUser.setPassword(encoder.encode(existingUser.getPassword()));
			existingUser.setUpdateDate(new Date());
		}	
		
		List<String> strRoles = userDto.getRole();
		userRoleRepo.deleteByUserId(userDto.getId()); // delete all previous role
		existingUser.setRoles(getRoleData(strRoles));
		userRepo.save(existingUser);
		return  new GenericResponse(true, ResponseMessage.SAVE_SUCCESS);
		
	}
	
	private Set<Role> getRoleData(List<String> strRoles) {
		Set<Role> roles = new HashSet<>();
		if (strRoles == null) {
			Role userRole = roleRepo.findByName(ERole.ROLE_STUDENT)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "ROLE_STUDENT":
					Role studentRole = roleRepo.findByName(ERole.ROLE_STUDENT)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(studentRole);

					break;
				case "ROLE_ADMIN":
					Role adminRole = roleRepo.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
					
				case "ROLE_SUPER_ADMIN":
					Role superAdminRole = roleRepo.findByName(ERole.ROLE_SUPER_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(superAdminRole);

					break;
				default:
					Role userRole = roleRepo.findByName(ERole.ROLE_STUDENT)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}
		return roles;
	}
	
	@Override
	public GenericResponse createUser(UserDto userDto) throws ParseException {
		if(userDto.getUserName() == null) {
			return new GenericResponse(false, "Error: Invalid user name");
		}
	
		if(userDto.getId() == null) {
			User user = userDto.getUser();
			if (userRepo.existsByEmail(user.getUserName())) {
				return new GenericResponse(false, "Error: User name already exist");
			}		
			String password = "";		
			if(user.getPassword() == null) {
				password = commonUtil.generatePassword();
				user.setPassword( encoder.encode(password));
			} else {
				String encodePassword =  encoder.encode(user.getPassword());
				user.setPassword(encodePassword);
			}
			
			List<String> strRoles = userDto.getRole();		
			user.setRoles(getRoleData(strRoles));
			userRepo.save(user);
		} else {
			return updateUser(userDto);	 // update user
		}
		
		return new GenericResponse(true, ResponseMessage.SAVE_SUCCESS);
	}

	@Override
	public List<RoutePermission> getRoutePermissionByRoleNameList(List<String> roleNameList) {
		// TODO Auto-generated method stub	
		return  permissionRepo.getPermissionByRoleNameList(roleNameList).orElse(null);
	}

}
