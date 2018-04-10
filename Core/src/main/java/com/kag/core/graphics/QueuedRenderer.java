package com.kag.core.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.PriorityQueue;

/**
 * Handler class for rendering {@link RenderItem RenderItems}. This class allows for rendering operations to be ordered
 * by depth regardless of the type of asset being rendered. This class does not attempt to optimize rendering in any way
 * by reducing the number of state changes on the GPU, because that is outside the scope of this project.
 *
 * @author Kasper
 */
public class QueuedRenderer {

	private static QueuedRenderer instance;
	private PriorityQueue<RenderItem> renderItems;
	private SpriteBatch sb;
	private ShapeRenderer sr;
	private OrthographicCamera staticCamera;
	private OrthographicCamera dynamicCamera;

	private QueuedRenderer() {
		renderItems = new PriorityQueue<>();
		sb = new SpriteBatch();
		sr = new ShapeRenderer();
		sr.setAutoShapeType(true);

		staticCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		staticCamera.setToOrtho(true);
		staticCamera.position.x = Gdx.graphics.getWidth() / 2;
		staticCamera.position.y = Gdx.graphics.getHeight() / 2;
		staticCamera.update();

		dynamicCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		dynamicCamera.setToOrtho(true);
		dynamicCamera.position.x = Gdx.graphics.getWidth() / 2;
		dynamicCamera.position.y = Gdx.graphics.getHeight() / 2;
		dynamicCamera.update();
	}

	public static QueuedRenderer getInstance() {
		if (instance == null) {
			instance = new QueuedRenderer();
		}

		return instance;
	}

	public OrthographicCamera getStaticCamera() {
		return staticCamera;
	}

	public OrthographicCamera getDynamicCamera() {
		return dynamicCamera;
	}

	public void enqueue(RenderItem renderItem) {
		renderItems.add(renderItem);
	}

	public void render() {
		int items = renderItems.size();
		for (int i = 0; i < items; i++) {
			RenderItem renderItem = renderItems.poll();
			
			sb.setProjectionMatrix(renderItem.getCamera().combined);
			sr.setProjectionMatrix(renderItem.getCamera().combined);
			sb.begin();
			sr.begin();
			renderItem.doOperation(sb, sr);
			sr.end();
			sb.end();
		}
		renderItems.clear();
	}

	public void dispose() {
		sb.dispose();
		sr.dispose();
	}
}
