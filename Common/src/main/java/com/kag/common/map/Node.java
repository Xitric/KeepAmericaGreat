package com.kag.common.map;

/**
 * A class used to represent a point along a path between two tiles in the game map. Each node points towards the next
 * node in the path, such that the entire path can be generated knowing just the initial node. This is similar to a
 * linked list. Each node also stores information about the tile to which it corresponds, so that a consumer can find
 * the position of the node in the game map.
 */
public class Node {

	private final Tile tile;
	protected Node next;

	/**
	 * Constructs a new node for the specified tile, pointing towards the specified other node.
	 *
	 * @param tile the tile that represents the position of this node
	 * @param node the next node in the path
	 */
	public Node(Tile tile, Node node) {
		this.next = node;
		this.tile = tile;
	}

	/**
	 * Get the next node in the path that this node is a part of. This node can be used to determine where to move to
	 * next.
	 *
	 * @return the next node in the path
	 */
	public Node getNext() {
		return next;
	}

	/**
	 * Get the tile that this node corresponds to. This tile can be used to determine the position of this node in the
	 * game map.
	 *
	 * @return the tile that this node corresponds to
	 */
	public Tile getTile() {
		return tile;
	}
}
