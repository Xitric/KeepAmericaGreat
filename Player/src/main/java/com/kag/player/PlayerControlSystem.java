package com.kag.player;

import com.kag.common.data.GameData;
import com.kag.common.data.World;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.*;
import com.kag.common.entities.parts.gui.LabelPart;
import com.kag.common.spinterfaces.IAssetManager;
import com.kag.common.spinterfaces.IComponentLoader;
import com.kag.common.spinterfaces.IEntitySystem;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

@ServiceProviders(value = {
	@ServiceProvider(service = IEntitySystem.class)
	,
        @ServiceProvider(service = IComponentLoader.class)
})
public class PlayerControlSystem implements IEntitySystem, IComponentLoader {

	private static final Family FAMILY = Family.forAll(LifePart.class, CurrencyPart.class, PositionPart.class);

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
		CurrencyPart currencyPart = entity.getPart(CurrencyPart.class);

		playerHealthGui.getPart(LabelPart.class).setLabel(String.valueOf(lifePart.getHealth()));
		playerCurrencyGui.getPart(LabelPart.class).setLabel(String.valueOf(currencyPart.getCurrencyAmount()));

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
		CurrencyPart currencyPart = new CurrencyPart(100);
		AssetPart trumpTower = assetManager.createTexture(getClass().getResourceAsStream("/trumpTower.png"));
		trumpTower.setxOffset(-64);
		trumpTower.setyOffset(-64);

		player.addPart(new PositionPart(300, 300));
		player.addPart(new BoundingBoxPart(128, 128));
		player.addPart(lifePart);
		player.addPart(currencyPart);
		player.addPart(trumpTower);

		//Player gui
		AssetPart healthIcon = assetManager.createTexture(getClass().getResourceAsStream("/health.png"));
		healthIcon.setxOffset(-40);
		healthIcon.setyOffset(-16);
		healthIcon.setzIndex(10);
		LabelPart healthLabel = new LabelPart("Health: " + String.valueOf(lifePart.getHealth()));
		healthLabel.setzIndex(10);
		
		playerHealthGui.addPart(new AbsolutePositionPart(66 + 768, 42));
		playerHealthGui.addPart(healthIcon);
		playerHealthGui.addPart(healthLabel);
		
		AssetPart currencyIcon = assetManager.createTexture(getClass().getResourceAsStream("/coin.png"));
		currencyIcon.setxOffset(-40);
		currencyIcon.setyOffset(-16);
		currencyIcon.setzIndex(10);
		LabelPart currencyLabel = new LabelPart("C-Fire: " + String.valueOf(currencyPart.getCurrencyAmount()));
		currencyLabel.setzIndex(10);
		
		playerCurrencyGui.addPart(new AbsolutePositionPart(66 + 768, 86));
		playerCurrencyGui.addPart(currencyIcon);
		playerCurrencyGui.addPart(currencyLabel);
		
		AssetPart playerPanel = assetManager.createTexture(getClass().getResourceAsStream("/PlayerPanel.png"));
		playerPanel.setzIndex(5);
		
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
