package com.kag.towercontroller;

import com.kag.common.data.GameData;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.AbsolutePositionPart;
import com.kag.common.entities.parts.BoundingBoxPart;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.input.Mouse;
import com.kag.common.map.World;
import com.kag.common.spinterfaces.IComponentLoader;
import com.kag.common.spinterfaces.ISystem;
import com.kag.commonasset.ZIndex;
import com.kag.commonasset.entities.parts.AssetPart;
import com.kag.commonasset.entities.parts.LabelPart;
import com.kag.commonasset.spinterfaces.IAssetManager;
import com.kag.commonplayer.entities.parts.PlayerPart;
import com.kag.commontd.entities.parts.MoneyPart;
import com.kag.commontower.spinterfaces.ITowerService;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@ServiceProviders(value = {
		@ServiceProvider(service = IComponentLoader.class),
		@ServiceProvider(service = ISystem.class),
		@ServiceProvider(service = ITowerService.ITowerSelectionListener.class)
})
public class TowerSellManager implements IComponentLoader, ISystem, ITowerService.ITowerSelectionListener {

	private static final Family PLAYER_FAMILY = Family.forAll(PlayerPart.class, MoneyPart.class);

	private Entity sellButton;
	private int sellingPrice;
	private List<Consumer<World>> jobs;
	private boolean buttonVisible;

	@Override
	public void load(World world) {
		IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);

		AbsolutePositionPart positionPart = new AbsolutePositionPart(695, 592);
		AssetPart buttonIcon = assetManager.createTexture(getClass().getResourceAsStream("/WoodSign.png"));
		buttonIcon.setzIndex(ZIndex.GUI_SELLLABEL);
		buttonIcon.setxOffset(-buttonIcon.getWidth() / 2);
		buttonIcon.setyOffset(-buttonIcon.getHeight() / 2);
		BoundingBoxPart bboxPart = new BoundingBoxPart(buttonIcon.getWidth(), buttonIcon.getHeight());

		LabelPart buttonLabel = new LabelPart("", 14);
		buttonLabel.setzIndex(ZIndex.GUI_CURRENCY_ICON);
		buttonLabel.setxOffset(-42);

		sellButton = new Entity();
		sellButton.addPart(buttonIcon);
		sellButton.addPart(buttonLabel);
		sellButton.addPart(positionPart);
		sellButton.addPart(bboxPart);

		jobs = new ArrayList<>();
	}

	@Override
	public void dispose(World world) {
		world.removeEntity(sellButton);
		for (AssetPart asset : sellButton.getPartsDeep(AssetPart.class)) {
			asset.dispose();
		}
	}

	@Override
	public void update(float dt, World world, GameData gameData) {
		for (Consumer<World> job : jobs) {
			job.accept(world);
		}
		jobs.clear();

		if (buttonVisible && gameData.getMouse().isButtonPressed(Mouse.BUTTON_LEFT)) {
			if (world.isEntityAt(sellButton, gameData.getMouse().getX(), gameData.getMouse().getY())) {
				world.getEntitiesByFamily(PLAYER_FAMILY).stream().findFirst().ifPresent(player ->
						player.getPart(MoneyPart.class).setMoney(player.getPart(MoneyPart.class).getMoney() + sellingPrice));

				ITowerService service = Lookup.getDefault().lookup(ITowerService.class);
				Entity towerToSell = service.getSelectedTower();
				world.removeEntity(towerToSell);

				PositionPart positionPart = towerToSell.getPart(PositionPart.class);
				world.getTileAt(positionPart.getX(), positionPart.getY()).setWalkable(true);

				service.towerRemoved(service.getSelectedTower());
			}
		}
	}

	@Override
	public void towerSelected(Entity tower) {
		sellingPrice = (int) (tower.getPart(MoneyPart.class).getMoney() * 0.75);
		sellButton.getPart(LabelPart.class).setLabel("Sell for\n" + sellingPrice);
		buttonVisible = true;
		jobs.add(world -> world.addEntity(sellButton));
	}

	@Override
	public void towerDeselected(Entity tower) {
		buttonVisible = false;
		jobs.add(world -> world.removeEntity(sellButton));
	}

	@Override
	public int getPriority() {
		return UPDATE_PASS_1;
	}
}
