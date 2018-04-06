package com.kag.projectile;

import com.kag.common.data.GameData;
import com.kag.common.data.World;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.AssetPart;
import com.kag.common.entities.parts.BoundingBoxPart;
import com.kag.common.entities.parts.DamagePart;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.spinterfaces.IComponentLoader;
import com.kag.common.spinterfaces.IEntitySystem;
import com.kag.common.spinterfaces.IProjectile;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

import static java.lang.Math.PI;

@ServiceProviders(value = {
		@ServiceProvider(service = IEntitySystem.class),
		@ServiceProvider(service = IComponentLoader.class),
		@ServiceProvider(service = IProjectile.class),
})
public class ProjectileMasterSystem implements IEntitySystem, IComponentLoader, IProjectile {

	private static final Family PROJECTILE_FAMILY = Family.forAll(PositionPart.class, BoundingBoxPart.class, DamagePart.class);
	private int projectileWidth = 10;
	private int projectileHeight = 10;

	@Override
	public void load(World world) {

	}

	@Override
	public void dispose(World world) {

	}

	@Override
	public int getPriority() {
		return UPDATE_PASS_1;
	}

	@Override
	public void createProjectile(float x, float y, int damage, float movingSpeed, float rotation, World world, AssetPart asset) {
		Entity projectile = new Entity();

		PositionPart positionPart = new PositionPart(x, y);
		positionPart.setRotation(rotation);

		BoundingBoxPart boundingBoxPart = new BoundingBoxPart(asset.getWidth(), asset.getHeight());
		DamagePart damagePart = new DamagePart(damage, movingSpeed);
		asset.setzIndex(10);

		projectile.addPart(positionPart);
		projectile.addPart(boundingBoxPart);
		projectile.addPart(damagePart);
		projectile.addPart(asset);

		world.addEntity(projectile);
	}

	@Override
	public Family getFamily() {
		return PROJECTILE_FAMILY;
	}

	@Override
	public void update(float delta, Entity entity, World world, GameData gameData) {
		PositionPart positionPart = entity.getPart(PositionPart.class);
		DamagePart damagePart = entity.getPart(DamagePart.class);

		float x = positionPart.getX();
		float y = positionPart.getY();
		double dx = Math.cos(positionPart.getRotation() / 360.0 * 2 * PI) * damagePart.getMovingSpeed() * delta;
		double dy = Math.sin(positionPart.getRotation() / 360.0 * 2 * PI) * damagePart.getMovingSpeed() * delta;

		x += dx;
		y += dy;

		if (x < 0 || x > world.getGameMap().getWidth() * world.getGameMap().getTileWidth() ||
				y < 0 || y > world.getGameMap().getHeight() * world.getGameMap().getTileHeight()) {
			world.removeEntity(entity);
		}

		positionPart.setPos(x, y);
	}
}
