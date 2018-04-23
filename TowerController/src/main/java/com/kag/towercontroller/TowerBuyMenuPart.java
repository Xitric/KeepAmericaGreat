package com.kag.towercontroller;

import com.kag.common.entities.IPart;
import com.kag.commontower.spinterfaces.ITower;

public class TowerBuyMenuPart implements IPart {

	private final ITower towerFactory;

	public TowerBuyMenuPart(ITower towerFactory) {
		this.towerFactory = towerFactory;
	}

	public ITower getTowerFactory() {
		return towerFactory;
	}
}
