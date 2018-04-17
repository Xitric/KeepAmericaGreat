package com.kag.shootingtowers;

import com.kag.common.data.*;
import com.kag.common.data.math.Vector2f;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.*;
import com.kag.common.spinterfaces.IAssetManager;
import com.kag.common.spinterfaces.IAudioManager;
import com.kag.common.spinterfaces.IComponentLoader;
import com.kag.common.spinterfaces.IEntitySystem;
import com.kag.tdcommon.entities.parts.LifePart;
import com.kag.tdcommon.entities.parts.MoneyPart;
import com.kag.tdcommon.entities.parts.TowerPart;
import com.kag.tdcommon.entities.parts.WeaponPart;
import com.kag.tdcommon.spinterfaces.IProjectile;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import parts.RotationSpeedPart;
import parts.ShootingTowerPart;

import java.util.Collection;

@ServiceProviders(value = {
        @ServiceProvider(service = IEntitySystem.class),
        @ServiceProvider(service = IComponentLoader.class)
})
public class ShootingTowerControlSystem implements IEntitySystem, IComponentLoader {

	private static final Family FAMILY = Family.forAll(NamePart.class, PositionPart.class, BlockingPart.class, RotationSpeedPart.class, MoneyPart.class, WeaponPart.class);
	private static final Family ENEMY_FAMILY = Family.forAll(LifePart.class, PositionPart.class, BoundingBoxPart.class, BlockingPart.class);
	private static final Family SHOOTINGTOWER_FAMILY = Family.forAll(ShootingTowerPart.class);

	private ISound shootingSound;

	@Override
	public void load(World world) {
		IAudioManager audioManager = Lookup.getDefault().lookup(IAudioManager.class);
		shootingSound = audioManager.loadSound(getClass().getResourceAsStream("/shooting.wav"), "wav");
	}

	@Override
	public void dispose(World world) {
		for(Entity entity : world.getEntitiesByFamily(SHOOTINGTOWER_FAMILY)){
			PositionPart towerPositionPart = entity.getPart(PositionPart.class);
			Tile hoverTile = world.getGameMap().getTile((int) towerPositionPart.getX() / world.getGameMap().getTileWidth() , (int) towerPositionPart.getY() / world.getGameMap().getTileWidth());
			hoverTile.setWalkable(true);
			world.removeEntity(entity);
		}

		shootingSound.dispose();
	}

    @Override
    public Family getFamily() {
        return FAMILY;
    }

    @Override
    public void update(float delta, Entity entity, World world, GameData gameData) {
        WeaponPart weaponPart = entity.getPart(WeaponPart.class);
        weaponPart.addDelta(delta);

        Entity enemy = getNearestEnemy(world, entity);
        if (enemy != null) {
            rotateTower(entity, calculateRotationForTower(enemy, entity, delta));
        }

        float timeBetweenShot = 1 / weaponPart.getAttackSpeed();
        if (weaponPart.getTimeSinceLast() > timeBetweenShot) {
            if (enemy != null && Math.abs(rotationDifference(entity, enemy)) < 25) {
                shootAt(world, entity);
                weaponPart.addDelta(-timeBetweenShot);
            } else {
                weaponPart.addDelta(timeBetweenShot - weaponPart.getTimeSinceLast());
            }
        }
    }

    /**
     * Get the nearest enemy to the specified tower. This method considers only enemies that are within the range of the
     * tower. If no enemy is close enough, this method will return null.
     *
     * @param world the game world
     * @param tower the tower from which to find the nearest enemy
     * @return the enemy nearest to the tower, or null if no enemies are in range
     */
    private Entity getNearestEnemy(World world, Entity tower) {
        PositionPart towerPositionPart = tower.getPart(PositionPart.class);
        WeaponPart towerWeaponPart = tower.getPart(WeaponPart.class);

        Entity nearest = null;
        float shortestDist = towerWeaponPart.getRange();

        for (Entity enemy : world.getEntitiesByFamily(ENEMY_FAMILY)) {
            PositionPart enemyPositionPart = enemy.getPart(PositionPart.class);
            float distance = (float) Math.hypot(enemyPositionPart.getX() - towerPositionPart.getX(), enemyPositionPart.getY() - towerPositionPart.getY());

            if (distance <= shortestDist) {
                nearest = enemy;
                shortestDist = distance;
            }
        }

        return nearest;
    }

    private void shootAt(World world, Entity tower) {
	    IProjectile projectileImplementation = Lookup.getDefault().lookup(IProjectile.class);

        TowerPart towerPart = tower.getPart(TowerPart.class);
        WeaponPart weaponPart = tower.getPart(WeaponPart.class);
        PositionPart towerPositionPart = tower.getPart(PositionPart.class);

        //Calculate rotation
	    AssetPart turretPart = getTurretPart(tower);
        float rotationResult = turretPart.getRotation();

        IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
        //MAJOR MEMORY LEAK WTF!?
        AssetPart pAsset = assetManager.createTexture(towerPart.getiTower().getProjectileAsset(),0,0,towerPart.getiTower().getProjectileAsset().getWidth(),towerPart.getiTower().getProjectileAsset().getHeight());
        pAsset.setyOffset(- pAsset.getHeight() / 2);

        int turretLength = turretPart.getWidth() + turretPart.getxOffset();
        float turretEndX = (float) (Math.cos(rotationResult / 360 * 2 * Math.PI) * turretLength) + towerPositionPart.getX();
        float turretEndY = (float) (Math.sin(rotationResult / 360 * 2 * Math.PI) * turretLength) + towerPositionPart.getY();

        shootingSound.play();

        projectileImplementation.createProjectile(turretEndX, turretEndY, weaponPart.getDamage(), weaponPart.getProjectileSpeed(), rotationResult, world, pAsset);
    }

    private AssetPart getTurretPart(Entity tower) {
        Collection<? extends AssetPart> assetParts = tower.getPartsDeep(AssetPart.class);
        for (AssetPart assetPart : assetParts) {
            if (assetPart.getzIndex() == ZIndex.TOWER_TURRET.value) {
                return assetPart;
            }
        }
        return null;
    }

    private void rotateTower(Entity tower, float rotationResult) {
        //Only move tower according to rotationspeed
        getTurretPart(tower).setRotation(rotationResult);
    }


    private float calculateRotation(Entity enemy, Entity tower) {
        PositionPart enemyPositionPart = enemy.getPart(PositionPart.class);
        PositionPart towerPositionPart = tower.getPart(PositionPart.class);

        Vector2f move = new Vector2f(enemyPositionPart.getX() - towerPositionPart.getX(), enemyPositionPart.getY() - towerPositionPart.getY());
        Vector2f lookDir = move.normalize();
        float rotationPi = (float) Math.atan2(lookDir.det(Vector2f.AXIS_X), lookDir.dot(Vector2f.AXIS_X));
        return -(float) (rotationPi / (2 * Math.PI) * 360);
    }

    private float rotationDifference(Entity tower, Entity enemy) {
	    float towerRotation = getTurretPart(tower).getRotation();
	    float rotationToEnemy = calculateRotation(enemy, tower);

	    return (rotationToEnemy - towerRotation) % 360;
    }

    private float calculateRotationForTower(Entity enemy, Entity tower, float dt) {
        float rotationSpeed = tower.getPart(RotationSpeedPart.class).getRotationSpeed();
        float towerRotation = getTurretPart(tower).getRotation();

        float difference = rotationDifference(tower, enemy);
	    if(Math.abs(difference) > 180) {
        	difference -= 360;
        }
        float toRotate = Math.max(Math.min(rotationSpeed * dt, difference), -rotationSpeed*dt);

        return towerRotation + toRotate;
    }

    @Override
    public int getPriority() {
        return UPDATE_PASS_2;
    }
}
