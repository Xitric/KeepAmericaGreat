package com.kag.commonasset.entities.parts;


/**
 * Part defining a label in the graphical user interface. A label is a string of
 * text.
 */
public class LabelPart extends AssetPart {

	private String label;
	private int fontSize = 18;

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

	@Override
	public void dispose() {
		//No-op
	}
}
