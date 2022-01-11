package com.student.dto.common;

import java.io.Serializable;
import java.util.List;

import com.student.entity.RoutePermission;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class MenuConfigData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Menus> menus;
	private List<String> routeList;
	private String role;
	private String userName;
	private Long userId;
	private Long studentId;
	private String profileImage;

}
