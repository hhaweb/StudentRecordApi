package com.student.dto.common;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class JwtResponse {
	private String token;
	private String type = "Bearer";
	private boolean status;
	public JwtResponse(String token, boolean status) {
		super();
		this.token = token;
		this.status = status;
	}
	public JwtResponse(boolean status) {
		super();
		this.status = status;
	}

	
}
