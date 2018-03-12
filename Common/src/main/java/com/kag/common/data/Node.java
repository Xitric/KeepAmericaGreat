package com.kag.common.data;

public class Node {
    private Node next;
    private Tile tile;

    public Node(Tile tile, Node node){
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
