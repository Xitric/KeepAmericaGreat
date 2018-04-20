package com.kag.commontower.entities.parts;

import com.kag.common.entities.IPart;
import com.kag.commontower.spinterfaces.ITower;

public class TowerPart implements IPart {

	private final ITower iTower;

	public TowerPart(ITower itower) {
		this.iTower = itower;
	}

	public ITower getiTower() {
		return iTower;
	}
}
