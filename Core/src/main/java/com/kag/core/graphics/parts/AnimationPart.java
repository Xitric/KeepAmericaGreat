package com.kag.core.graphics.parts;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * @author Kasper
 */
public class AnimationPart extends TexturePart {

	private Animation animation;
	private float stateTime;

	public AnimationPart(Texture texture, Animation animation) {
		super(texture);
		this.animation = animation;
	}

	public Animation getAnimation() {
		return animation;
	}

	public float getStateTime() {
		return stateTime;
	}

	public void addStateTime(float stateTime) {
		this.stateTime += stateTime;
	}
}
