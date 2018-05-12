package com.kag.towercontroller;

import com.kag.common.entities.Entity;
import com.kag.commontower.spinterfaces.ITowerService;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = ITowerService.class)
public class TowerService implements ITowerService {

	private Entity selectedTower;

	@Override
	public void towerCreated(Entity tower) {
		towerSelected(null);

		for (ITowerCreationListener listener : Lookup.getDefault().lookupAll(ITowerCreationListener.class)) {
			listener.towerCreated(tower);
		}

		towerSelected(tower);
	}

	@Override
	public void towerRemoved(Entity tower) {
		if (tower == selectedTower) {
			towerDeselected(tower);
		}

		for (ITowerCreationListener listener : Lookup.getDefault().lookupAll(ITowerCreationListener.class)) {
			listener.towerRemoved(tower);
		}
	}

	@Override
	public void towerSelected(Entity tower) {
		if (tower == selectedTower) return;

		towerDeselected(selectedTower);
		selectedTower = tower;

		if (tower != null) {
			for (ITowerSelectionListener listener : Lookup.getDefault().lookupAll(ITowerSelectionListener.class)) {
				listener.towerSelected(tower);
			}
		}
	}

	@Override
	public void towerDeselected(Entity tower) {
		selectedTower = null;

		if (tower != null) {
			for (ITowerSelectionListener listener : Lookup.getDefault().lookupAll(ITowerSelectionListener.class)) {
				listener.towerDeselected(tower);
			}
		}
	}

	@Override
	public Entity getSelectedTower() {
		return selectedTower;
	}
}
