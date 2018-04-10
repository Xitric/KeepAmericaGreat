package com.kag.core.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Matrix4;
import com.kag.common.data.GameData;
import com.kag.common.data.World;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.AbsolutePositionPart;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.entities.parts.gui.LabelPart;
import com.kag.common.spinterfaces.IComponentLoader;
import com.kag.common.spinterfaces.IEntitySystem;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * System for rendering labels that are part of the graphical user interface.
 * This system must be placed in the core module, since this is the only module
 * that is able to use LibGDX. Labels are strings of text that have a position
 * on the screen Thus this system only processes entities with the following
 * parts:
 * <ul>
 * <li>{@link LabelPart}</li>
 * <li>{@link PositionPart}</li>
 * </ul>
 *
 * @author Kasper
 */
@ServiceProviders(value = {
		@ServiceProvider(service = IEntitySystem.class),
		@ServiceProvider(service = IComponentLoader.class)
})
public class LabelSystem implements IEntitySystem, IComponentLoader {

	private static final Family FAMILY = Family.forAll(LabelPart.class, AbsolutePositionPart.class);

	private FreeTypeFontGenerator fontGenerator;
	private Map<Integer, BitmapFont> fonts;
	private GlyphLayout glyphLayout;

	@Override
	public void load(World world) {
		fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font/BD_Cartoon_Shout.ttf"));
		fonts = new HashMap<>();
		glyphLayout = new GlyphLayout();
	}

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
		AbsolutePositionPart position = entity.getPart(AbsolutePositionPart.class);

		OrthographicCamera cam = QueuedRenderer.getInstance().getStaticCamera();

		for (LabelPart label : labelParts) {
			RenderItem renderItem = new RenderItem(label.getzIndex(), cam, (sb, sr) -> {
				sb.setTransformMatrix(new Matrix4().idt());
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
