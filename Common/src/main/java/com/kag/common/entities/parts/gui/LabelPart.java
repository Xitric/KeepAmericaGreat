package com.kag.common.entities.parts.gui;

import com.kag.common.entities.IPart;

/**
 * Part defining a label in the graphical user interface. A label is a string of
 * text.
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
