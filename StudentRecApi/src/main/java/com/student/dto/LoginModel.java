package com.student.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String userName;
	public String password;
}
