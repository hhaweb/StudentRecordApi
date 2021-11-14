package com.student.dto.common;

import java.util.List;

import com.student.dto.Menus;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class JwtResponse {
	private String token;
	private String type = "Bearer";
	private Long id;
	private String userName;
	private List<String> roles;
	private List<Menus> menuList;
	private boolean status;
	
	public JwtResponse(boolean status, String token, Long id, String userName,List<String> roles, List<Menus> menuList) {
		super();
		this.status = status;
		this.token = token;
		this.id = id;
		this.userName = userName;
		this.roles = roles;
		this.menuList = menuList;
	}

	public JwtResponse(boolean status) {
		super();
		this.status = status;
	}
	
	
	
	
}
