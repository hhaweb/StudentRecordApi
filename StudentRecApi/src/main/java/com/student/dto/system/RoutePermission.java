package com.student.dto.system;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoutePermission {
	private String routerLink;
	private Boolean writeAccess;
}
