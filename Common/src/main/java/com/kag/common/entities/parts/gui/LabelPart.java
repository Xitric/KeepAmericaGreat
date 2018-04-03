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
	private int fontSize = 18;
	private int zIndex;

	public LabelPart(String label) {
		this.label = label;
	}

	public LabelPart(String label, int fontSize) {
		this(label);
		this.fontSize = fontSize;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public int getzIndex() {
		return zIndex;
	}

	public void setzIndex(int zIndex) {
		this.zIndex = zIndex;
	}
}
