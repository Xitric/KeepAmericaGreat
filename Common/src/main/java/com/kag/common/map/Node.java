package com.kag.common.map;



public class Node {

	protected Node next;
	private final Tile tile;

	public Node(Tile tile, Node node) {
		this.next = node;
		this.tile = tile;
	}

	public Node getNext() {
		return next;
	}

	public Tile getTile() {
		return tile;
	}
}
