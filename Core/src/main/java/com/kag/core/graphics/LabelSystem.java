package com.kag.core.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.kag.common.data.GameData;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.AbsolutePositionPart;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.map.World;
import com.kag.common.spinterfaces.IComponentLoader;
import com.kag.common.spinterfaces.IEntitySystem;
import com.kag.commonasset.entities.parts.LabelPart;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * System for rendering labels that are part of the graphical user interface. This system must be placed in the core
 * module, since this is the only module that is able to use LibGDX. Labels are strings of text that have a position on
 * the screen. Thus this system only processes entities with the following parts:
 * <ul>
 * <li>{@link LabelPart}</li>
 * </ul>
 * And at least one of these parts:
 * <ul>
 * <li>{@link PositionPart}</li>
 * <li>{@link AbsolutePositionPart}</li>
 * </ul>
 *
 * @author Kasper
 */
@ServiceProviders(value = {
		@ServiceProvider(service = IEntitySystem.class),
		@ServiceProvider(service = IComponentLoader.class)
})
public class LabelSystem extends AbstractRenderer implements IEntitySystem, IComponentLoader {

	private static final Family FAMILY = Family.forAll(LabelPart.class)
			.includingAny(PositionPart.class, AbsolutePositionPart.class);

	private FreeTypeFontGenerator fontGenerator;
	private Map<Integer, BitmapFont> fonts;
	private GlyphLayout glyphLayout;

	@Override
	public void load(World world) {
		fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font/BD_Cartoon_Shout.ttf"));
		fonts = new HashMap<>();
		glyphLayout = new GlyphLayout();
	}

	/**
	 * Get a bitmap font for the specified font size. If such a font does not yet exist, it will be generated and stored
	 * for later use.
	 *
	 * @param size the font size to get a font for
	 * @return the font for the specified size
	 */
	private BitmapFont getFontForSize(int size) {
		BitmapFont font = fonts.get(size);

		if (font == null) {
			FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
			parameter.size = size;
			parameter.flip = true;
			font = fontGenerator.generateFont(parameter);
			fonts.put(size, font);
		}

		return font;
	}

	@Override
	public void dispose(World world) {
		fonts.values().forEach(BitmapFont::dispose);
		fonts.clear();
		fontGenerator.dispose();
	}

	@Override
	public void update(float delta, Entity entity, World world, GameData gameData) {
		Collection<LabelPart> labelParts = entity.getParts(LabelPart.class);
		PositionPart position = getPosition(entity);
		OrthographicCamera cam = getCamera(entity);

		for (LabelPart label : labelParts) {
			//Apply local rotation around origin, and then apply entity rotation
			Matrix4 transform = new Matrix4();
			transform.idt()
//					.translate(position.getX(), position.getY(), 0)
					.rotate(Vector3.Z, position.getRotation())
					.translate(label.getxOffset(), label.getyOffset(), 0)
					.translate(label.getOriginX(), label.getOriginY(), 0)
					.rotate(Vector3.Z, label.getRotation())
					.translate(-label.getOriginX(), -label.getOriginY(), 0);

			SpriteRenderItem renderItem = new SpriteRenderItem(label.getzIndex(), cam, sb -> {
				sb.setTransformMatrix(transform);
				BitmapFont font = getFontForSize(label.getFontSize());
				glyphLayout.setText(font, label.getLabel());
				font.draw(sb, glyphLayout, position.getX(), position.getY() - glyphLayout.height / 2);
			});

			QueuedRenderer.getInstance().enqueue(renderItem);
		}
	}

	@Override
	public Family getFamily() {
		return FAMILY;
	}

	@Override
	public int getPriority() {
		return RENDER_PASS;
	}
}
