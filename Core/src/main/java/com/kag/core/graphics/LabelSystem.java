package com.kag.core.graphics;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
	@ServiceProvider(service = IEntitySystem.class)
	,
	@ServiceProvider(service = IComponentLoader.class)
})
public class LabelSystem implements IEntitySystem, IComponentLoader {

	private static final Family FAMILY = Family.forAll(LabelPart.class, AbsolutePositionPart.class);

	private BitmapFont font;

	@Override
	public void load(World world) {
		font = new BitmapFont(true);
	}

	@Override
	public void dispose(World world) {
		font.dispose();
	}

	@Override
	public void update(float delta, Entity entity, World world, GameData gameData) {
		LabelPart label = entity.getPart(LabelPart.class);
		AbsolutePositionPart position = entity.getPart(AbsolutePositionPart.class);

		OrthographicCamera cam = QueuedRenderer.getInstance().getStaticCamera();
		RenderItem renderItem = new RenderItem(label.getzIndex(), cam, sb -> {
			font.draw(sb, label.getLabel(), position.getX(), position.getY());
		});

		QueuedRenderer.getInstance().enqueue(renderItem);
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
