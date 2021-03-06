package com.kag.projectile;

import com.kag.common.data.GameData;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.BoundingBoxPart;
import com.kag.common.entities.parts.MovingPart;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.map.World;
import com.kag.common.spinterfaces.ICollision;
import com.kag.common.spinterfaces.IComponentLoader;
import com.kag.common.spinterfaces.IEntitySystem;
import com.kag.commonasset.ZIndex;
import com.kag.commonasset.entities.parts.AssetPart;
import com.kag.commonenemy.entities.parts.EnemyPart;
import com.kag.commonprojectile.entities.parts.DamagePart;
import com.kag.commonprojectile.spinterfaces.IProjectile;
import com.kag.commontd.entities.parts.LifePart;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

import static java.lang.Math.PI;

@ServiceProviders(value = {
		@ServiceProvider(service = IEntitySystem.class),
		@ServiceProvider(service = IComponentLoader.class),
		@ServiceProvider(service = IProjectile.class),
})
public class ProjectileMasterSystem implements IEntitySystem, IComponentLoader, IProjectile {

	private static final Family PROJECTILE_FAMILY = Family.forAll(PositionPart.class, BoundingBoxPart.class, DamagePart.class, MovingPart.class);
	private static final Family ENEMY_FAMILY = Family.forAll(PositionPart.class, BoundingBoxPart.class, LifePart.class, EnemyPart.class);

	@Override
	public void load(World world) {

	}

	@Override
	public void dispose(World world) {
		for (Entity projectile : world.getEntitiesByFamily(PROJECTILE_FAMILY)) {
			world.removeEntity(projectile);
		}
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
		DamagePart damagePart = new DamagePart(damage);
		MovingPart movingPart = new MovingPart(movingSpeed);
		asset.setzIndex(ZIndex.TOWER_PROJECTILES);

		projectile.addPart(positionPart);
		projectile.addPart(boundingBoxPart);
		projectile.addPart(damagePart);
		projectile.addPart(movingPart);
		projectile.addPart(asset);

		world.addEntity(projectile);
	}

	@Override
	public Family getFamily() {
		return PROJECTILE_FAMILY;
	}

	@Override
	public void update(float delta, Entity entity, World world, GameData gameData) {
		PositionPart projectilePositionPart = entity.getPart(PositionPart.class);
		DamagePart towerDamagePart = entity.getPart(DamagePart.class);
		MovingPart projectileMovingPart = entity.getPart(MovingPart.class);

		double dx = Math.cos(projectilePositionPart.getRotation() / 360.0 * 2 * PI) * projectileMovingPart.getMovementSpeed() * delta;
		double dy = Math.sin(projectilePositionPart.getRotation() / 360.0 * 2 * PI) * projectileMovingPart.getMovementSpeed() * delta;

		float x = projectilePositionPart.getX();
		float y = projectilePositionPart.getY();

		Entity closestEntity = null;
		float closestEntityDistance = 100;

		for (Entity entityInWorld : world.getEntitiesByFamily(ENEMY_FAMILY)) {
			PositionPart enemyPositionPart = entityInWorld.getPart(PositionPart.class);
			float distance = (float) Math.hypot(enemyPositionPart.getX() - projectilePositionPart.getX(), enemyPositionPart.getY() - projectilePositionPart.getY());

			if (distance < closestEntityDistance) {
				closestEntityDistance = distance;
				closestEntity = entityInWorld;
			}
		}

		ICollision collision = Lookup.getDefault().lookup(ICollision.class);
		if (closestEntity != null) {
			if (collision != null && collision.doesCollide(entity, closestEntity)) {
				LifePart enemyLifePart = closestEntity.getPart(LifePart.class);
				enemyLifePart.setHealth(enemyLifePart.getHealth() - towerDamagePart.getDamage());

				world.removeEntity(entity);
			}
		}

		x += dx;
		y += dy;

		if (x < 0 || x > world.getGameMap().getWidth() * world.getGameMap().getTileWidth() ||
				y < 0 || y > world.getGameMap().getHeight() * world.getGameMap().getTileHeight()) {
			world.removeEntity(entity);
		}

		projectilePositionPart.setPos(x, y);
	}
}

