package com.kag.towercontroller;

import com.kag.common.data.Color;
import com.kag.common.data.World;
import com.kag.common.data.ZIndex;
import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.CirclePart;
import com.kag.common.spinterfaces.IComponentLoader;
import com.kag.tdcommon.entities.parts.WeaponPart;
import com.kag.tdcommon.spinterfaces.ITowerService;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 * @author Kasper
 */
@ServiceProviders(value = {
		@ServiceProvider(service = IComponentLoader.class),
		@ServiceProvider(service = ITowerService.ITowerSelectionListener.class)
})
public class TowerSelectionInformationManager implements IComponentLoader, ITowerService.ITowerSelectionListener {

	private CirclePart rangeCircle;

	@Override
	public void load(World world) {
		rangeCircle = new CirclePart(0, new Color(0x55ADD8E6));
		rangeCircle.setzIndex(ZIndex.TOWER_RANGE_PREVIEW);
	}

	@Override
	public void dispose(World world) {
		Entity tower = Lookup.getDefault().lookup(ITowerService.class).getSelectedTower();
		if (tower != null) {
			tower.removePart(rangeCircle);
		}
	}

	@Override
	public void towerSelected(Entity tower) {
		WeaponPart weaponPart = tower.getPart(WeaponPart.class);
		if (weaponPart != null) {
			rangeCircle.setRadius(weaponPart.getRange());
			tower.addPart(rangeCircle);
		}
	}

	@Override
	public void towerDeselected(Entity tower) {
		tower.removePart(rangeCircle);
	}
}
