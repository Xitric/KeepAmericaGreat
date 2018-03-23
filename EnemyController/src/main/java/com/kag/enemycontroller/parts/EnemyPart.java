package com.kag.enemycontroller.parts;

import com.kag.common.data.Node;
import com.kag.common.entities.IPart;

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
