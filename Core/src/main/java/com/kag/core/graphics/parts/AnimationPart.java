package com.kag.core.graphics.parts;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.kag.commonasset.entities.parts.AssetPart;
import com.kag.commonasset.spinterfaces.IAsset;

public class AnimationPart extends AssetPart {

	private final IAsset asset;
	private final Animation animation;
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
