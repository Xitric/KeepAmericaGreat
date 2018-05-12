package com.kag.pathfinder;

import com.kag.common.map.GameMap;
import com.kag.common.map.Node;
import com.kag.common.map.World;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Kasper
 */
public class DijkstraTest {

	private World world;
	private int goalX;
	private int goalY;
	private int expectedLength;

	@org.junit.Before
	public void setUp() throws Exception {
		//Arrange
		world = new World(new GameMap(8, 8, 0, 0));

		//Construct test map. 0 means walkable, 1 means not walkable
		//# is the goal
		//0 0 1 0 0 0 0 0
		//0 0 1 0 0 0 0 0
		//0 0 1 0 # 1 0 0
		//0 0 0 0 0 1 0 0
		//1 1 1 1 1 1 0 0
		//0 0 0 1 0 0 0 0
		//1 1 1 1 0 0 0 0
		//0 0 0 0 0 0 0 0
		world.getGameMap().getTile(2, 0).setWalkable(false);
		world.getGameMap().getTile(2, 1).setWalkable(false);
		world.getGameMap().getTile(2, 2).setWalkable(false);

		world.getGameMap().getTile(0, 4).setWalkable(false);
		world.getGameMap().getTile(1, 4).setWalkable(false);
		world.getGameMap().getTile(2, 4).setWalkable(false);
		world.getGameMap().getTile(3, 4).setWalkable(false);
		world.getGameMap().getTile(4, 4).setWalkable(false);
		world.getGameMap().getTile(5, 4).setWalkable(false);
		world.getGameMap().getTile(5, 3).setWalkable(false);
		world.getGameMap().getTile(5, 2).setWalkable(false);

		world.getGameMap().getTile(0, 6).setWalkable(false);
		world.getGameMap().getTile(1, 6).setWalkable(false);
		world.getGameMap().getTile(2, 6).setWalkable(false);
		world.getGameMap().getTile(3, 6).setWalkable(false);
		world.getGameMap().getTile(3, 5).setWalkable(false);

		goalX = 4;
		goalY = 2;
		expectedLength = 10;
	}

	@Test
	public void constructNodeMap() {
		//Act
		Dijkstra dijkstra = new Dijkstra();
		Node[][] nodes = dijkstra.constructNodeMap(goalX, goalY, world);

		//Assert
		//TC#1
		Node current = nodes[7][7];
		int length = 0;
		while (current.getNext() != null) {
			current = current.getNext();
			length++;
		}

		Assert.assertTrue(current.getTile().getX() == goalX);
		Assert.assertTrue(current.getTile().getY() == goalY);
		Assert.assertTrue(length == expectedLength);

		//TC#2
		Assert.assertNull(nodes[5][1]);

		//TC#3
		Assert.assertNull(nodes[4][1]);
	}
}