package com.student.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.student.config.ConfigData;
import com.student.config.ERole;
import com.student.dto.LoginModel;
import com.student.dto.StudentDto;
import com.student.dto.UserDetailsImpl;
import com.student.dto.UserDto;
import com.student.dto.common.GenericResponse;
import com.student.dto.common.JwtResponse;
import com.student.dto.common.MenuConfigData;
import com.student.dto.common.MenuItem;
import com.student.dto.common.Menus;
import com.student.dto.common.ResponseMessage;
import com.student.dto.common.SearchDto;
import com.student.dto.common.SelectedItem;
import com.student.entity.Role;
import com.student.entity.RoutePermission;
import com.student.entity.User;
import com.student.repository.UserRepository;
import com.student.service.StudentService;
import com.student.service.UserService;
import com.student.service.impl.UserDetailsServiceImpl;
import com.student.util.CommonUtil;
import com.student.util.JwtUtils;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	UserRepository userRepository;
	@Autowired
	UserService userService;
	@Autowired
	JwtUtils jwtUtils;
	@Autowired
	CommonUtil commonUtils;

	@Autowired
	StudentService studentService;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;


	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginModel loginRequest) {
		Authentication authentication = null;
		boolean isFirstTime = false;
		try {
			authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
		} catch (Exception e) { // try with plain text password for first time login
			UserDetails userDetails = userService.findByUserNameAndPlainPassword(loginRequest.getUserName(), loginRequest.getPassword());
			if(userDetails == null ) {
				return ResponseEntity.ok(new JwtResponse(false));
			}
			authentication = new UsernamePasswordAuthenticationToken(
			userDetails, null, userDetails.getAuthorities());	
			isFirstTime = true;
		}
		SecurityContextHolder.getContext().setAuthentication(authentication); // to save login user authencation info
		String jwt = jwtUtils.generateJwtToken(authentication);
		return ResponseEntity.ok(new JwtResponse(jwt, true, isFirstTime));
	}

	@PostMapping("/save-user")
	public GenericResponse createUser(@RequestBody UserDto userDto) {
		try {
			if (userDto.getUserName() != null) {
				return userService.createUser(userDto);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new GenericResponse(false, ResponseMessage.INTERNAL_ERROR);
		}
		return null;
	}

	@GetMapping("/delete-user")
	public GenericResponse deleteUser(@RequestParam("userId") String userId) {
		try {
			Long id = Long.parseLong(userId);
			return userService.deleteUser(id);
		} catch (Exception e) {
			e.printStackTrace();
			return new GenericResponse(false, ResponseMessage.DELETE_FAIL);// TODO: handle exception
		}
	}

	@GetMapping("/get-system-config")
	public MenuConfigData getMenuConfig() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		MenuConfigData configData = new MenuConfigData();
		String roleName = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList()).get(0);
		List<Menus> fullMenuList = commonUtils.getMenuItem();
		List<Menus> roleMenuList = new ArrayList<Menus>();
		List<String> menuList = new ArrayList<String>();
		List<String> routeList = new ArrayList<String>();

		if (roleName.equalsIgnoreCase("ROLE_SUPER_ADMIN")) {
			menuList = ConfigData.Super_Admin;
			routeList = ConfigData.Super_Admin_Route;
		} else if (roleName.equalsIgnoreCase("ROLE_ADMIN")) {
			menuList = ConfigData.Admin;
			routeList = ConfigData.Admin_Route;
		}
		for (Menus menu : fullMenuList) {
			if (menuList.size() > 0) {
				List<MenuItem> childItem = new ArrayList<MenuItem>();
				for (MenuItem item : menu.getItems()) {
					String childLabel = item.getLabel();
					int i = menuList.indexOf(childLabel);
					if (i != -1) {
						childItem.add(item);
					}
				}

				if (childItem.size() > 0) {
					menu.setItems(childItem);
					roleMenuList.add(menu);
				}
			}
		}

		Long studentId = (long) 0;
		String profileImage = null;
		StudentDto studentDto = studentService.getStudentByCid(userDetails.getUserName());

		if (studentDto != null) {
			profileImage = studentDto.getBase64Image() != null ? studentDto.getBase64Image()
					: studentDto.getAvatar() != null ? studentDto.getAvatar() : null;
			if (studentDto != null) {
				studentId = studentDto.getId();
			}
		}

		configData.setMenus(roleMenuList);
		configData.setRouteList(routeList);
		configData.setRole(roleName);
		configData.setUserId(userDetails.getId());
		configData.setStudentId(studentId);
		configData.setProfileImage(profileImage);
		configData.setUserName(userDetails.getUserName());
		return configData;
	}

	@GetMapping("/get-current-user")
	public UserDto getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		if (userDetails != null) {
			UserDto userDto = new UserDto(userDetails);
			return userDto;
		}
		return null;

	}

	@PostMapping("/get-user-list")
	public List<UserDto> getUserList(@Valid @RequestBody SearchDto search) {
		List<UserDto> userDtoList = null;
		try {
			userDtoList = userService.getUserListWithPager(search);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userDtoList;
	}

	@GetMapping("get-user")
	public UserDto getUserById(@RequestParam("userId") String id) {
		Long userId = Long.parseLong(id);
		UserDto userDto = userService.getUserById(userId);
		return userDto;
	}

	@GetMapping("get-roles")
	public List<SelectedItem> getRoles() {
		List<SelectedItem> itemList = new ArrayList<SelectedItem>();
		List<Role> roleList = userService.getRole();
		for (Role role : roleList) {
			SelectedItem item = new SelectedItem(role.getName().toString(), role.getId().toString(), false);
			itemList.add(item);
		}
		return itemList;

	}
	
	@GetMapping("change-password")
	public GenericResponse changePassword(@RequestParam("password") String password) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		if (userDetails != null) {
			UserDto userDto = new UserDto(userDetails);
			userDto.setPassword(password);
			return userService.changePasswordForFirstTimeLogin(userDto);
		}
		return new GenericResponse(false, "");
	}
}
