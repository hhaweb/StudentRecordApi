package com.student.dto.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SelectedItem {
	private String label;
	private Object value;
	private boolean disabled;
	public SelectedItem(String label, Object value, boolean disabled) {
		super();
		this.label = label;
		this.value = value;
		this.disabled = disabled;
	}
	
	
}
