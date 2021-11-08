package com.student.dto.system;

import java.util.List;

import com.student.entity.user.RoutePermission;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class ConfigData {
	private List<Menus> menus;
	private List<RoutePermission> routePermissions;

}
