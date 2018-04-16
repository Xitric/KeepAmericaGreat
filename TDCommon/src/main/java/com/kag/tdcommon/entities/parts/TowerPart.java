package com.kag.tdcommon.entities.parts;

import com.kag.common.entities.IPart;
import com.kag.tdcommon.spinterfaces.ITower;

public class TowerPart implements IPart {

	private ITower iTower;

	public TowerPart(ITower itower) {
		this.iTower = itower;
	}

	public ITower getiTower() {
		return iTower;
	}
}
