package com.kag.core.graphics;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.function.Consumer;

/**
 * A rendering operation for drawing 2d textures using a sprite batch.
 */
public class SpriteRenderItem extends RenderItem {

	private final Consumer<SpriteBatch> operation;

	public SpriteRenderItem(int zIndex, OrthographicCamera camera, Consumer<SpriteBatch> operation) {
		super(zIndex, camera);
		this.operation = operation;
	}

	public void doOperation(SpriteBatch sb) {
		operation.accept(sb);
	}
}
