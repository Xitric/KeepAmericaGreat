package com.kag.commonenemywalking.entities.parts;

import com.kag.common.entities.IPart;
import com.kag.common.map.Node;

public class WalkingPart implements IPart {

	private Node nextNode;

	public Node getNextNode() {
		return nextNode;
	}

	public void setNextNode(Node nextNode) {
		this.nextNode = nextNode;
	}
}
