package com.kag.core.graphics.parts;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.kag.common.data.IAsset;
import com.kag.common.entities.parts.AssetPart;

/**
 * @author Kasper
 */
public class AnimationPart extends AssetPart {

	private IAsset asset;
	private Animation animation;
	private float stateTime;

	public AnimationPart(IAsset asset, Animation animation) {
		this.asset = asset;
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

	@Override
	public void dispose() {
		asset.dispose();
	}
}
