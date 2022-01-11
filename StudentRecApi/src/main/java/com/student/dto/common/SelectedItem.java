package com.student.dto.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SelectedItem {
	private String label;
	private String value;
	private boolean disabled;
	
	
	public SelectedItem(String label, String value, boolean disabled) {
		super();
		this.label = label;
		this.value = value;
		this.disabled = disabled;
	}


	public SelectedItem(String label, String value) {
		super();
		this.label = label;
		this.value = value;
	}
	
	
}
