package com.kag.enemycontroller;

import com.kag.common.data.GameData;
import com.kag.common.data.GameMap;
import com.kag.common.data.Node;
import com.kag.common.data.World;
import com.kag.common.data.math.Vector2f;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.spinterfaces.IEntitySystem;
import com.kag.common.spinterfaces.IPathFinder;
import com.kag.enemycontroller.parts.EnemyPart;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 * @author Kasper
 */
@ServiceProvider(service = IEntitySystem.class)
public class EnemyMovingSystem implements IEntitySystem {

	private static final Family FAMILY = Family.forAll(EnemyPart.class, PositionPart.class);

	@Override
	public void update(float delta, Entity entity, World world, GameData gameData) {
		EnemyPart enemyPart = entity.getPart(EnemyPart.class);

		if (enemyPart.getNextNode() == null) {
			generateNewPath(entity, world);
		}

		if (enemyPart.getNextNode() != null) {
			move(entity, delta, world.getGameMap().getTileWidth(), world.getGameMap().getTileHeight());
		}
	}

	private void generateNewPath(Entity entity, World world) {
		//TODO: Just look it up in the game map when Dijkstra has been implemented
		IPathFinder pathFinder = Lookup.getDefault().lookup(IPathFinder.class);
		if (pathFinder == null) return;

		GameMap map = world.getGameMap();

		PositionPart positionPart = entity.getPart(PositionPart.class);
		int tileX = (int) (positionPart.getX() / map.getTileWidth());
		int tileY = (int) (positionPart.getY() / map.getTileHeight());

		Node nextNode = pathFinder.getPath(tileX, tileY, map.getPlayerX(), map.getPlayerY(), world);
		entity.getPart(EnemyPart.class).setNextNode(nextNode);
	}

	private void move(Entity entity, float dt, int tileWidth, int tileHeight) {
		PositionPart positionPart = entity.getPart(PositionPart.class);
		EnemyPart enemyPart = entity.getPart(EnemyPart.class);
		Node nextNode = enemyPart.getNextNode();

		Vector2f position = new Vector2f(positionPart.getX(), positionPart.getY());
		Vector2f goal = new Vector2f(nextNode.getTile().getX() * tileWidth + tileWidth / 2,
				nextNode.getTile().getY() * tileHeight + tileHeight / 2);

		Vector2f moveDirection = goal.sub(position);
		Vector2f move = Vector2f.ZERO;
		if (!moveDirection.isZero()) {
			move = moveDirection.normalize().scale(enemyPart.getSpeed() * dt);
		}

		if (move.lengthSquared() > moveDirection.lengthSquared()) {
			//We are able to reach past the current goal node, so we check if we can go to the next node instead
			nextNode = nextNode.getNext();
			enemyPart.setNextNode(nextNode);

			if (nextNode == null) {
				//We reached the final goal, so just go to it
				move = moveDirection;
			} else {
				goal = new Vector2f(nextNode.getTile().getX() * tileWidth + tileWidth / 2,
						nextNode.getTile().getY() * tileHeight + tileHeight / 2);
				move = goal.sub(position).normalize().scale(enemyPart.getSpeed() * dt);
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

	@Override
	public Family getFamily() {
		return FAMILY;
	}

	@Override
	public int getPriority() {
		return 0;
	}
}