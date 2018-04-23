package com.kag.enemywalkingcontroller;

import com.kag.common.data.GameData;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.BoundingBoxPart;
import com.kag.common.entities.parts.MovingPart;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.map.Node;
import com.kag.common.map.World;
import com.kag.common.spinterfaces.ICollision;
import com.kag.common.spinterfaces.IEntitySystem;
import com.kag.common.spinterfaces.IPathFinder;
import com.kag.common.spinterfaces.IPrioritizable;
import com.kag.commonenemy.entities.parts.EnemyPart;
import com.kag.commonenemywalking.entities.parts.WalkingPart;
import com.kag.commonmath.Vector2f;
import com.kag.commonplayer.entities.parts.PlayerPart;
import com.kag.commontd.entities.parts.LifePart;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IEntitySystem.class)
public class EnemyMovingSystem implements IEntitySystem {

	private static final Family FAMILY = Family.forAll(EnemyPart.class, WalkingPart.class, MovingPart.class, PositionPart.class, BoundingBoxPart.class);
	private static final Family PLAYER_FAMILY = Family.forAll(PlayerPart.class, PositionPart.class, BoundingBoxPart.class, LifePart.class);

	@Override
	public void update(float delta, Entity entity, World world, GameData gameData) {
		WalkingPart walkingPart = entity.getPart(WalkingPart.class);

		if (walkingPart.getNextNode() == null) {
			generateNewPath(entity, world);
		}

		if (walkingPart.getNextNode() != null) {
			move(entity, delta, world, world.getGameMap().getTileWidth(), world.getGameMap().getTileHeight());
		}

		checkReachedGoal(entity, world);
	}

	private void generateNewPath(Entity entity, World world) {
		PositionPart positionPart = entity.getPart(PositionPart.class);
		int tileX = (int) (positionPart.getX() / world.getGameMap().getTileWidth());
		int tileY = (int) (positionPart.getY() / world.getGameMap().getTileHeight());

		entity.getPart(WalkingPart.class).setNextNode(world.getGameMap().getPathNodes()[tileY][tileX]);
	}

	private void move(Entity entity, float dt, World world, int tileWidth, int tileHeight) {
		PositionPart positionPart = entity.getPart(PositionPart.class);
		WalkingPart walkingPart = entity.getPart(WalkingPart.class);
		Node nextNode = walkingPart.getNextNode();

		if(world.getGameMap().doesCollideWithTile(nextNode.getTile(), entity)) {
			if(!nextNode.getTile().isWalkable()) {
				walkingPart.setNextNode(null);

				//Recalculate node map - it has become invalid
				IPathFinder pathFinder = Lookup.getDefault().lookup(IPathFinder.class);
				if (pathFinder == null) return;

				Node[][] nodeMap = pathFinder.getPath(world.getGameMap().getPlayerX(), world.getGameMap().getPlayerY(), world);
				world.getGameMap().setPathNodes(nodeMap);

				return;
			}
		}

		Vector2f position = new Vector2f(positionPart.getX(), positionPart.getY());
		Vector2f goal = new Vector2f(nextNode.getTile().getX() * tileWidth + tileWidth / 2,
				nextNode.getTile().getY() * tileHeight + tileHeight / 2);

		Vector2f moveDirection = goal.sub(position);
		Vector2f move = Vector2f.ZERO;
		if (!moveDirection.isZero()) {
			move = moveDirection.normalize().scale(entity.getPart(MovingPart.class).getMovementSpeed() * dt);
		} else {
			//Bug fix: Some enemies would randomly stop mid path
			walkingPart.setNextNode(walkingPart.getNextNode().getNext());
		}

		if (move.lengthSquared() > moveDirection.lengthSquared()) {
			//We are able to reach past the current goal node, so we check if we can go to the next node instead
			nextNode = nextNode.getNext();
			walkingPart.setNextNode(nextNode);

			if (nextNode == null) {
				//We reached the final goal, so just go to it
				move = moveDirection;
			} else {
				goal = new Vector2f(nextNode.getTile().getX() * tileWidth + tileWidth / 2,
						nextNode.getTile().getY() * tileHeight + tileHeight / 2);
				move = goal.sub(position).normalize().scale(entity.getPart(MovingPart.class).getMovementSpeed() * dt);
			}
		}

		Vector2f newPosition = position.add(move);

		positionPart.setPos(newPosition.x, newPosition.y);

		//Calculate enemy rotation
		if (! move.isZero()) {
			Vector2f lookDir = move.normalize();
			float rotationPi = (float) Math.atan2(lookDir.det(Vector2f.AXIS_X), lookDir.dot(Vector2f.AXIS_X));
			entity.getPart(PositionPart.class).setRotation(-(float) (rotationPi / (2 * Math.PI) * 360));
		}
	}

	private void checkReachedGoal(Entity entity, World world) {
		Entity trumpTower = world.getEntitiesByFamily(PLAYER_FAMILY).stream().findFirst().orElse(null);

		if(trumpTower == null) {
			return;
		}

		ICollision collision = Lookup.getDefault().lookup(ICollision.class);
		if (collision.doesCollide(entity, trumpTower)) {
			LifePart pHealth = trumpTower.getPart(LifePart.class);
			pHealth.setHealth(pHealth.getHealth() - 1);
			world.removeEntity(entity);
		}
	}

	@Override
	public Family getFamily() {
		return FAMILY;
	}

	@Override
	public int getPriority() {
		return IPrioritizable.UPDATE_PASS_1;
	}
}
