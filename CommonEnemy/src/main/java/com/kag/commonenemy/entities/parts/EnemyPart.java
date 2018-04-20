package com.kag.commonenemy.entities.parts;

import com.kag.common.entities.IPart;
import com.kag.common.map.Node;

/**
 * @author Kasper
 */
public class EnemyPart implements IPart {

	private Node nextNode;
	private float speed;

	public EnemyPart(float speed) {
		this.speed = speed;
	}

	public Node getNextNode() {
		return nextNode;
	}

	public void setNextNode(Node nextNode) {
		this.nextNode = nextNode;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
}
