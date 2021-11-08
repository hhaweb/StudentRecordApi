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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.student.config.ERole;
import com.student.dto.common.GenericResponse;
import com.student.dto.common.ResponseMessage;
import com.student.dto.system.ConfigData;
import com.student.dto.system.MenuItem;
import com.student.dto.system.Menus;
import com.student.dto.user.JwtResponse;
import com.student.dto.user.LoginModel;
import com.student.dto.user.UserDetailsImpl;
import com.student.dto.user.UserDto;
import com.student.entity.user.RoutePermission;
import com.student.entity.user.User;
import com.student.repository.user.UserRepository;
import com.student.service.user.UserService;
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
	PasswordEncoder encoder;
	
	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginModel loginRequest) {
		Authentication authentication = null;
		
	try {
		 authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
	}catch(Exception e) {
		e.printStackTrace();
	}
		

		SecurityContextHolder.getContext().setAuthentication(authentication); // to save login user authencation info
		
		String jwt = jwtUtils.generateJwtToken(authentication);
		List<Menus> menu =  commonUtils.getMenuItem();
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		
		return ResponseEntity.ok(new JwtResponse(jwt, 
												 userDetails.getId(), 
												 userDetails.getUsername(), 
												 userDetails.getEmail(),
												 roles,
												 menu
												 ));
	}
	
	@PostMapping("/signup")
	public GenericResponse createUser(@RequestBody UserDto userDto) {
		try {
			if(userDto.getUserName() != null) {
				return userService.createUser(userDto);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new GenericResponse(false, ResponseMessage.INTERNAL_ERROR);
		}
		return null;
	}
	
	@GetMapping("/get-system-config")
	public ConfigData getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		ConfigData configData = new ConfigData();
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());	
		List<Menus> fullMenuList =  commonUtils.getMenuItem();
		
		List<RoutePermission>  routePermissionList = userService.getRoutePermissionByRoleNameList(roles);		
		
	
		List<Menus> filterMenuList = new ArrayList<Menus>();
		for (RoutePermission permission : routePermissionList) {
			if(permission != null) {
				filterMenuList.add(getMenus(permission.getRouterLink(), fullMenuList));
			}
		}
		
		configData.setRoutePermissions(userService.getRoutePermissionByRoleNameList(roles));
		configData.setMenus(filterMenuList);
		return configData;
	}
	
	private Menus getMenus(String routerLink, List<Menus> menuList) {
		Menus m = new Menus();
		for (Menus menu : menuList) {
			if(menu != null) {
				if(menu.getRouterLink() != null && menu.getRouterLink().size() > 0 && menu.getRouterLink().get(0).equalsIgnoreCase(routerLink)) {
					m = menu;
				}
				List<MenuItem> itemList = new ArrayList<MenuItem>();
				if(menu.getItems() != null) {
					for (MenuItem item : menu.getItems()) {
						if(item.getRouterLink() != null && item.getRouterLink().size() > 0 && item.getRouterLink().get(0).equalsIgnoreCase(routerLink)) {
							itemList.add(item);
						}
					}
				}
			
				if(itemList.size() > 0) {
					m.setIcon(menu.getIcon());
					m.setLabel(menu.getLabel());
				}
				if(m.getItems() == null) {
					m.setItems(new ArrayList<MenuItem>());
				}
				m.setItems(itemList);
			}
		}
		return m;		
	}
}