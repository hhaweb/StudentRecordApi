package com.student.dto;

import java.io.Serializable;
import java.util.List;

import com.student.entity.RoutePermission;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class ConfigData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Menus> menus;
	private List<RoutePermission> routePermissions;

}
