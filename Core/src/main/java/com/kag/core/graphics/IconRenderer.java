package com.kag.core.graphics;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.kag.common.data.GameData;
import com.kag.common.data.World;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.AbsolutePositionPart;
import com.kag.common.spinterfaces.IEntitySystem;
import com.kag.core.graphics.parts.TexturePart;
import org.openide.util.lookup.ServiceProvider;

/**
 * @author Kasper
 */
@ServiceProvider(service = IEntitySystem.class)
public class IconRenderer implements IEntitySystem {

	private static final Family FAMILY = Family.forAll(AbsolutePositionPart.class, TexturePart.class);

	@Override
	public void update(float delta, Entity entity, World world, GameData gameData) {
		TexturePart texturePart = entity.getPart(TexturePart.class);
		Texture texture = texturePart.getTexture();
		AbsolutePositionPart position = entity.getPart(AbsolutePositionPart.class);

		OrthographicCamera cam = QueuedRenderer.getInstance().getStaticCamera();
		RenderItem renderItem = new RenderItem(texturePart.getzIndex(), cam, sb -> {
			sb.draw(texture,
					position.getX() + texturePart.getxOffset(),
					position.getY() + texturePart.getyOffset(),
					texturePart.getWidth(), texturePart.getHeight(),
					0, 0,
					texture.getWidth(), texture.getHeight(),
					false, true);
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
