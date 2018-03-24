package com.kag.enemycontroller;

import com.kag.common.data.GameData;
import com.kag.common.data.Node;
import com.kag.common.data.World;
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
	public Family getFamily() {
		return FAMILY;
	}

	@Override
	public void update(float delta, Entity entity, World world, GameData gameData) {
		EnemyPart enemyPart = entity.getPart(EnemyPart.class);
		PositionPart positionPart = entity.getPart(PositionPart.class);

		if (enemyPart.getNextNode() == null) {
			IPathFinder pathfinder = Lookup.getDefault().lookup(IPathFinder.class);
			if (pathfinder != null) {
				Node nextNode = pathfinder.getPath((int) positionPart.getX() / 64, (int) positionPart.getY() / 64, 6, 35, world);
				if (nextNode == null) System.out.println("No valid path!!!");
				enemyPart.setNextNode(nextNode);
			}
		}

		Node nextNode = enemyPart.getNextNode();

		if (nextNode != null) {
			float dx = nextNode.getTile().getX() * 64 - positionPart.getX();
			float dy = nextNode.getTile().getY() * 64 - positionPart.getY();

			if (dx != 0) dx /= Math.abs(dx);
			if (dy != 0) dy /= Math.abs(dy);

			//TODO: Can be a lot better - we should consider using vectors ;)
			positionPart.setPos(positionPart.getX() + delta * dx * enemyPart.getSpeed(), positionPart.getY() + delta * dy * enemyPart.getSpeed());

			if (atNode(positionPart, nextNode)) {
				enemyPart.setNextNode(nextNode.getNext());
			}
		}
	}

	private boolean atNode(PositionPart position, Node node) {
		return Math.abs(position.getX() - node.getTile().getX() * 64) + Math.abs(position.getY() - node.getTile().getY() * 64) < 5;
	}

	@Override
	public int getPriority() {
		return 0;
	}
}
