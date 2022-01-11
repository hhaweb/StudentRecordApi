package com.student.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.student.config.ERole;
import com.student.dto.UserDto;
import com.student.dto.common.GenericResponse;
import com.student.dto.common.ResponseMessage;
import com.student.dto.common.SearchDto;
import com.student.entity.Role;
import com.student.entity.RoutePermission;
import com.student.entity.User;
import com.student.entity.User;
import com.student.repository.PermissionRepository;
import com.student.repository.RoleRepository;
import com.student.repository.UserRepository;
import com.student.service.UserService;
import com.student.util.CSVHelper;
import com.student.util.CommonUtil;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepo;
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
		if (existingUser != null) {
			User existingEmail = userRepo.findByUserName(userDto.getUserName()).orElse(null);
			if (existingEmail != null && existingEmail.getId() != userDto.getId()) {
				return new GenericResponse(false, "Error: Email already exist");
			}
			existingUser.setUserName(userDto.getUserName());
			if(userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
				existingUser.setPassword(encoder.encode(userDto.getPassword()));
			}
			existingUser.setUpdateDate(new Date());
		}
		if (userDto.getRoleId() != null && userDto.getRoleId() > 0) {
			Role role = roleRepo.findById(userDto.getRoleId()).orElse(null);
			existingUser.setRole(role);
		}

		userRepo.save(existingUser);
		return new GenericResponse(existingUser.getId().toString(), true, ResponseMessage.SAVE_SUCCESS);

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
		if (userDto.getUserName() == null) {
			return new GenericResponse(false, "Error: Invalid user name");
		}
		if(userDto.getRoleId() == null || userDto.getRoleId() == 0) {
			return new GenericResponse(false, "Error: Please add role");
		}
		User saveObj = null;
		if (userDto.getId() == null) {
			User user = userDto.getEntity();
			if (userRepo.existsByEmail(user.getUserName())) {
				return new GenericResponse(false, "Error: User name already exist");
			}
			String password = "";
			if (user.getPassword() == null) {
				password = commonUtil.generatePassword();
				user.setPassword(encoder.encode(password));
			} else {
				String encodePassword = encoder.encode(user.getPassword());
				user.setPassword(encodePassword);
			}
			if (userDto.getRoleId() != null && userDto.getRoleId() > 0) {
				Role role = roleRepo.findById(userDto.getRoleId()).orElse(null);
				user.setRole(role);
			}
		
			saveObj = userRepo.saveAndFlush(user);
		} else {
			return updateUser(userDto); // update user
		}

		return new GenericResponse(saveObj.getId().toString(), true, ResponseMessage.SAVE_SUCCESS);
	}

	@Override
	public List<RoutePermission> getRoutePermissionByRoleNameList(List<String> roleNameList) {
		// TODO Auto-generated method stub
		return permissionRepo.getPermissionByRoleNameList(roleNameList).orElse(null);
	}

	@Override
	public List<UserDto> getUserListWithPager(SearchDto searchDto) {
		List<UserDto> userDtoList = new ArrayList<UserDto>();
		int pageNo = searchDto.getRowOffset();
		int pageSize = searchDto.getRowsPerPage();
		String sortBy = searchDto.getSortName();
		Pageable paging = PageRequest.of(pageNo, pageSize,
				searchDto.getSortType() == 1 ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
		List<User> UserList = new ArrayList<User>();
		if (searchDto.getSearchKeyword() != null && !searchDto.getSearchKeyword().isEmpty()) {
			Long id = CSVHelper.isNumeric(searchDto.getSearchKeyword()) ? Long.parseLong(searchDto.getSearchKeyword())
					: 0;
			UserList = userRepo.getUserByPager(id, searchDto.getSearchKeyword(), paging);
		} else {
			UserList = userRepo.findAll(paging).toList();
		}

		if (UserList.size() > 0) {
			for (User User : UserList) {
				userDtoList.add(new UserDto(User));
			}
			userDtoList.get(0).setTotalRecord(userRepo.getTotalRecord());
		}

		return userDtoList;
	}

	@Override
	public UserDto getUserById(Long id) {
		UserDto userDto = null;
		User user = userRepo.findById(id).orElse(null);
		if (user != null) {
			userDto = new UserDto(user);
		}
		return userDto;
	}

	@Override
	public List<Role> getRole() {
		return roleRepo.findAll();
	}

}
