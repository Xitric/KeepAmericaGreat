package com.kag.towercontroller;

import com.kag.common.data.World;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.spinterfaces.IGameStateListener;
import com.kag.tdcommon.entities.parts.TowerPart;
import com.kag.tdcommon.spinterfaces.ITowerService;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 * @author Kasper
 */
@ServiceProvider(service = IGameStateListener.class)
public class TowerGameReset implements IGameStateListener {

	private static final Family TOWER_FAMILY = Family.forAll(TowerPart.class);

	@Override
	public void newGame(World world) {
		ITowerService service = Lookup.getDefault().lookup(ITowerService.class);

		for (Entity tower : world.getEntitiesByFamily(TOWER_FAMILY)) {
			world.removeEntity(tower);
			service.towerRemoved(tower);
		}
	}
}
