package com.kag.common.entities.parts.gui;

import com.kag.common.entities.IPart;

/**
 *
 * @author Kasper
 */
public class LabelPart implements IPart {
	
	private String label;
	
	public LabelPart(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
}
