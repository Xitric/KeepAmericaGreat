package com.kag.core.graphics;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import java.util.function.Consumer;

/**
 * A rendering operation for drawing geometric shapes using a shape renderer. Instances of this class with the same
 * z-index will also be ordered by shape type, as this minimizes the number of flushes on the GPU.
 */
public class ShapeRenderItem extends RenderItem {

	private final ShapeType drawType;
	private final Consumer<ShapeRenderer> operation;

	public ShapeRenderItem(int zIndex, OrthographicCamera camera, ShapeType drawType, Consumer<ShapeRenderer> operation) {
		super(zIndex, camera);
		this.drawType = drawType;
		this.operation = operation;
	}

	public ShapeType getDrawType() {
		return drawType;
	}

	public void doOperation(ShapeRenderer sr) {
		operation.accept(sr);
	}

	@Override
	public int compareTo(RenderItem that) {
		int result = Integer.compare(this.getzIndex(), that.getzIndex());

		//Sort by shape type within same layer - this way we can make fewer flushes to the GPU
		if (result == 0 && that instanceof ShapeRenderItem) {
			ShapeRenderItem thatShape = (ShapeRenderItem) that;
			result = Integer.compare(drawType.getGlType(), thatShape.getDrawType().getGlType());
		}

		//If still the same, sort by insertion index
		if (result == 0) {
			result = Integer.compare(this.getInsertionIndex(), that.getInsertionIndex());
		}

		return result;
	}
}
