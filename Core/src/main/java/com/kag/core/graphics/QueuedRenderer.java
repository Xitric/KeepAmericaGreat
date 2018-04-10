package com.kag.core.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.PriorityQueue;

/**
 * Handler class for rendering {@link RenderItem RenderItems}. This class allows for rendering operations to be ordered
 * by depth regardless of the type of asset being rendered. This class makes only minimal attempts to reduce state
 * changes and flushes on the GPU.
 *
 * @author Kasper
 */
public class QueuedRenderer {

	private static QueuedRenderer instance;
	private PriorityQueue<SpriteRenderItem> spriteRenderItems;
	private PriorityQueue<ShapeRenderItem> shapeRenderItems;
	private SpriteBatch sb;
	private ShapeRenderer sr;
	private OrthographicCamera staticCamera;
	private OrthographicCamera dynamicCamera;

	private QueuedRenderer() {
		spriteRenderItems = new PriorityQueue<>();
		shapeRenderItems = new PriorityQueue<>();

		sb = new SpriteBatch();
		sr = new ShapeRenderer();

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

	/**
	 * Get the camera used to render graphical elements that have an absolute position on the screen such as GUI
	 * elements.
	 *
	 * @return the camera used to render elements with an absolute position
	 */
	public OrthographicCamera getStaticCamera() {
		return staticCamera;
	}

	/**
	 * Get the camera used to render graphical elements that have a position in world coordinates and whose position on
	 * the screen changes as the game camera moves.
	 *
	 * @return the camera used to render elements with world coordinates
	 */
	public OrthographicCamera getDynamicCamera() {
		return dynamicCamera;
	}

	/**
	 * Schedule a rendering job for drawing sprites.
	 *
	 * @param renderItem the rendering job
	 */
	public void enqueue(SpriteRenderItem renderItem) {
		renderItem.setInsertionIndex(spriteRenderItems.size());
		spriteRenderItems.add(renderItem);
	}

	/**
	 * Schedule a rendering job for drawing geometric objects.
	 *
	 * @param renderItem the rendering job
	 */
	public void enqueue(ShapeRenderItem renderItem) {
		renderItem.setInsertionIndex(shapeRenderItems.size());
		shapeRenderItems.add(renderItem);
	}

	/**
	 * Render all jobs scheduled in this manager since the last invocation of this method. Rendering jobs are performed
	 * in order of their z-index.
	 */
	public void render() {
		int zIndex = getMinimumZIndex();

		//State variables to help minimize state changes and flushes on GPU
		OrthographicCamera prevSpriteCam = null;
		OrthographicCamera prevShapeCam = null;
		ShapeRenderer.ShapeType prevShapeType = null;

		while (!(spriteRenderItems.isEmpty() && shapeRenderItems.isEmpty())) {

			//Render one layer of sprites
			while (!spriteRenderItems.isEmpty() && spriteRenderItems.peek().getzIndex() == zIndex) {
				//Ensure that we are not using both the sprite batch and shape renderer ate the same time
				if (sr.isDrawing()) sr.end();

				SpriteRenderItem spriteRenderItem = spriteRenderItems.poll();

				//Change state if necessary
				if (spriteRenderItem.getCamera() != prevSpriteCam) {
					prevSpriteCam = spriteRenderItem.getCamera();
					sb.setProjectionMatrix(spriteRenderItem.getCamera().combined);
				}

				//Begin sprite batch if not already begun. Important to do this after setting states to prevent flushing
				//nothing
				if (!sb.isDrawing()) sb.begin();
				spriteRenderItem.doOperation(sb);
			}

			//Render the same layer of geometric shapes
			while (!shapeRenderItems.isEmpty() && shapeRenderItems.peek().getzIndex() == zIndex) {
				//Ensure that we are not using both the sprite batch and shape renderer ate the same time
				if (sb.isDrawing()) sb.end();

				ShapeRenderItem shapeRenderItem = shapeRenderItems.poll();

				//Change state if necessary
				if (shapeRenderItem.getCamera() != prevShapeCam) {
					prevShapeCam = shapeRenderItem.getCamera();
					sr.setProjectionMatrix(shapeRenderItem.getCamera().combined);
				}

				if (shapeRenderItem.getDrawType() != prevShapeType) {
					prevShapeType = shapeRenderItem.getDrawType();
					//Ending the shape renderer means that the if statement below will begin it with the new shape type
					if (sr.isDrawing()) sr.end();
				}

				//Begin shape renderer if not already begun. Important to do this after setting states to prevent
				//flushing nothing
				if (!sr.isDrawing()) sr.begin(shapeRenderItem.getDrawType());
				shapeRenderItem.doOperation(sr);
			}

			zIndex = getMinimumZIndex();
		}

		//Flush remaining render operations
		if (sb.isDrawing()) sb.end();
		if (sr.isDrawing()) sr.end();
	}

	/**
	 * Get the minimum z-index among all rendering jobs. If there are no jobs, this method will return
	 * {@link Integer#MAX_VALUE}.
	 *
	 * @return the minimum z-index of all jobs or {@link Integer#MAX_VALUE} if there are no jobs
	 */
	private int getMinimumZIndex() {
		int spriteMin = spriteRenderItems.isEmpty() ? Integer.MAX_VALUE : spriteRenderItems.peek().getzIndex();
		int shapeMin = shapeRenderItems.isEmpty() ? Integer.MAX_VALUE : shapeRenderItems.peek().getzIndex();

		return Math.min(spriteMin, shapeMin);
	}

	public void dispose() {
		sb.dispose();
		sr.dispose();
	}
}
