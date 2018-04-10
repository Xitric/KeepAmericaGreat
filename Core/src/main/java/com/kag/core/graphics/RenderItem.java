package com.kag.core.graphics;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.function.BiConsumer;

/**
 * Representation of a rendering operation with LibGDX which can be queued up. The point of queuing render operations is
 * that different types of assets (which are handled by separate entity systems) might need to be rendered at
 * interleaving layers.
 *
 * @author Kasper
 */
public class RenderItem implements Comparable<RenderItem> {

	private int zIndex;
	private BiConsumer<SpriteBatch, ShapeRenderer> operation;
	private OrthographicCamera camera;

	public RenderItem(int zIndex, OrthographicCamera camera, BiConsumer<SpriteBatch, ShapeRenderer> operation) {
		this.zIndex = zIndex;
		this.camera = camera;
		this.operation = operation;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public void doOperation(SpriteBatch sb, ShapeRenderer sr) {
		operation.accept(sb, sr);
	}

	@Override
	public int compareTo(RenderItem that) {
		return Integer.compare(this.zIndex, that.zIndex);
	}
}
