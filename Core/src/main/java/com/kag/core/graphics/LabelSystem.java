package com.kag.core.graphics;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kag.common.data.GameData;
import com.kag.common.data.World;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
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

	private static final Family family = Family.forAll(LabelPart.class, PositionPart.class);

	private SpriteBatch sb;
	private BitmapFont font;

	@Override
	public void load(World world) {
		sb = new SpriteBatch();
		font = new BitmapFont();
	}

	@Override
	public void dispose(World world) {
		font.dispose();
		sb.dispose();
	}

	@Override
	public void update(float delta, Entity entity, World world, GameData gameData) {
		LabelPart label = entity.getPart(LabelPart.class);
		PositionPart position = entity.getPart(PositionPart.class);
		
		sb.begin();
		font.draw(sb, label.getLabel(), position.getX(), position.getY());
		sb.end();
	}

	@Override
	public Family getFamily() {
		return family;
	}

	@Override
	public int getPriority() {
		return 100;
	}
}
