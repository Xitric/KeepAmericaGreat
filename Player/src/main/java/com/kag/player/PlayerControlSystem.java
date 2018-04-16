package com.kag.player;

import com.kag.common.data.GameData;
import com.kag.common.data.World;
import com.kag.common.data.ZIndex;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.AbsolutePositionPart;
import com.kag.common.entities.parts.AssetPart;
import com.kag.common.entities.parts.BoundingBoxPart;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.entities.parts.gui.LabelPart;
import com.kag.common.spinterfaces.IAssetManager;
import com.kag.common.spinterfaces.IComponentLoader;
import com.kag.common.spinterfaces.IEntitySystem;
import com.kag.tdcommon.entities.parts.LifePart;
import com.kag.tdcommon.entities.parts.MoneyPart;
import com.kag.tdcommon.entities.parts.PlayerPart;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

@ServiceProviders(value = {
		@ServiceProvider(service = IEntitySystem.class),
		@ServiceProvider(service = IComponentLoader.class)
})
public class PlayerControlSystem implements IEntitySystem, IComponentLoader {

	private static final Family FAMILY = Family.forAll(LifePart.class, MoneyPart.class, PositionPart.class, PlayerPart.class);

	private Entity player;
	private Entity playerHealthGui;
	private Entity playerCurrencyGui;
	private Entity playerMenuBackground;

	@Override
	public Family getFamily() {
		return FAMILY;
	}

	@Override
	public void update(float delta, Entity entity, World world, GameData gameData) {
		LifePart lifePart = entity.getPart(LifePart.class);
		MoneyPart currencyPart = entity.getPart(MoneyPart.class);

		playerHealthGui.getPart(LabelPart.class).setLabel(String.valueOf(lifePart.getHealth()));
		playerCurrencyGui.getPart(LabelPart.class).setLabel(String.valueOf(currencyPart.getMoney()));

	}

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void load(World world) {
		player = new Entity();
		playerHealthGui = new Entity();
		playerCurrencyGui = new Entity();
		playerMenuBackground = new Entity();

		IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);

		//Player entity
		LifePart lifePart = new LifePart(50);
		MoneyPart currencyPart = new MoneyPart(100);
		AssetPart trumpTower = assetManager.createTexture(getClass().getResourceAsStream("/trumpTower.png"));
		trumpTower.setxOffset(-64);
		trumpTower.setyOffset(-64);
		trumpTower.setzIndex(ZIndex.TRUMP_TOWER);

		player.addPart(new PositionPart(world.getGameMap().getPlayerX() * world.getGameMap().getTileWidth(),
				world.getGameMap().getPlayerY() * world.getGameMap().getTileHeight()));
		player.addPart(new BoundingBoxPart(128, 128));
		player.addPart(lifePart);
		player.addPart(currencyPart);
		player.addPart(trumpTower);
		player.addPart(new PlayerPart());

		//Player gui
		AssetPart healthIcon = assetManager.createTexture(getClass().getResourceAsStream("/health.png"));
		healthIcon.setxOffset(-40);
		healthIcon.setyOffset(-16);
		healthIcon.setzIndex(ZIndex.GUI_HEALTH_ICON);
		LabelPart healthLabel = new LabelPart("Health: " + String.valueOf(lifePart.getHealth()));
		healthLabel.setzIndex(ZIndex.GUI_HEALTH_LABEL);

		playerHealthGui.addPart(new AbsolutePositionPart(66 + 768, 42));
		playerHealthGui.addPart(healthIcon);
		playerHealthGui.addPart(healthLabel);

		AssetPart moneyIcon = assetManager.createTexture(getClass().getResourceAsStream("/coin.png"));
		moneyIcon.setxOffset(-40);
		moneyIcon.setyOffset(-16);
		moneyIcon.setzIndex(ZIndex.GUI_CURRENCY_ICON);
		LabelPart moneyLabel = new LabelPart("C-Fire: " + String.valueOf(currencyPart.getMoney()));
		moneyLabel.setzIndex(ZIndex.GUI_CURRENCY_LABEL);

		playerCurrencyGui.addPart(new AbsolutePositionPart(66 + 768, 86));
		playerCurrencyGui.addPart(moneyIcon);
		playerCurrencyGui.addPart(moneyLabel);

		AssetPart playerPanel = assetManager.createTexture(getClass().getResourceAsStream("/PlayerPanel.png"));
		playerPanel.setzIndex(ZIndex.GUI_PLAYER_PANEL);

		playerMenuBackground.addPart(new AbsolutePositionPart(768, 0));
		playerMenuBackground.addPart(playerPanel);

		world.addEntity(player);
		world.addEntity(playerHealthGui);
		world.addEntity(playerCurrencyGui);
		world.addEntity(playerMenuBackground);
	}

	@Override
	public void dispose(World world) {
		world.removeEntity(player);
		world.removeEntity(playerHealthGui);
		world.removeEntity(playerCurrencyGui);
		world.removeEntity(playerMenuBackground);
	}
}
