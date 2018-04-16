package com.kag.tdcommon.spinterfaces;

import com.kag.common.data.World;
import com.kag.common.entities.parts.AssetPart;

/**
 * Interface describing a service for constructing projectile entities in the world.
 */
public interface IProjectile {

	/**
	 * Constructs a new projectile entity and adds it to the world.
	 *
	 * @param x           the initial x position of the projectile in pixels
	 * @param y           the initial y position of the projectile in pixels
	 * @param damage      the damage of the projectile
	 * @param movingSpeed the speed at which the projectile moves in pixels per second
	 * @param rotation    the rotation of the projectile in degrees
	 * @param world       the game world
	 * @param asset       the graphical asset used to represent the projectile
	 */
	void createProjectile(float x, float y, int damage, float movingSpeed, float rotation, World world, AssetPart asset);
}
