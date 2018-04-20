package com.kag.common.entities.parts;

import com.kag.common.entities.parts.PositionPart;

/**
 * @author Kasper
 */
public class AbsolutePositionPart extends PositionPart {

	public AbsolutePositionPart(float x, float y) {
		super(x, y);
	}

	public AbsolutePositionPart(float x, float y, int rotation) {
		super(x, y, rotation);
	}
}
