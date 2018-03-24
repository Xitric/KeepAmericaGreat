package com.kag.core.graphics.parts;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * @author Kasper
 */
public class AnimationPart extends TexturePart {

	private Animation animation;

	public AnimationPart(Texture texture, Animation animation) {
		super(texture);
		this.animation = animation;
	}

	public Animation getAnimation() {
		return animation;
	}
}
