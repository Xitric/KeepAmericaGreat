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
    private Entity playerHealthLabel;
    private Entity playerCurrencyLabel;
    private Entity playerMenuBackground;
    private Entity playerHealthIcon;
    private Entity playerCurrencyIcon;

    @Override
    public Family getFamily() {
        return FAMILY;
    }

    @Override
    public void update(float delta, Entity entity, World world, GameData gameData) {
        LifePart lifePart = entity.getPart(LifePart.class);
        CurrencyPart currencyPart = entity.getPart(CurrencyPart.class);

        playerHealthLabel.getPart(LabelPart.class).setLabel(String.valueOf(lifePart.getHealth()));
        playerCurrencyLabel.getPart(LabelPart.class).setLabel(String.valueOf(currencyPart.getCurrencyAmount()));

    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void load(World world) {
        player = new Entity();
        playerHealthLabel = new Entity();
        playerCurrencyLabel = new Entity();
        playerMenuBackground = new Entity();
        playerHealthIcon = new Entity();
        playerCurrencyIcon = new Entity();

        IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);

        LifePart lifePart = new LifePart(50);
        CurrencyPart currencyPart = new CurrencyPart(100);

        player.addPart(new PositionPart(300, 300));
        player.addPart(new BoundingBoxPart(128, 128));
        player.addPart(lifePart);
        player.addPart(currencyPart);
        player.addPart(lifePart);
        player.addPart(assetManager.createTexture(getClass().getResourceAsStream("/trumpTower.png")));

        playerHealthIcon.addPart(assetManager.createTexture(getClass().getResourceAsStream("/lifeIcon.png")));
        playerHealthIcon.addPart(new AbsolutePositionPart(810, 597));

        playerHealthLabel.addPart(new AbsolutePositionPart(870, 620));
        playerHealthLabel.addPart(lifePart);
        playerHealthLabel.addPart(new LabelPart("Health: " + String.valueOf(lifePart.getHealth())));

        playerCurrencyIcon.addPart(assetManager.createTexture(getClass().getResourceAsStream("/coinIcon.png")));
        playerCurrencyIcon.addPart(new AbsolutePositionPart(810, 537));

        playerCurrencyLabel.addPart(new AbsolutePositionPart(870, 560));
        playerCurrencyLabel.addPart(currencyPart);
        LabelPart lblPt = new LabelPart("C-Fire: " + String.valueOf(currencyPart.getCurrencyAmount()));
        lblPt.setzIndex(10);
        playerCurrencyLabel.addPart(lblPt);

        playerMenuBackground.addPart(new AbsolutePositionPart(768, 520));
        playerMenuBackground.addPart(assetManager.createTexture(getClass().getResourceAsStream("/red_panel.png")));

        //world.addEntity(player); Add trump
        world.addEntity(playerHealthLabel);
        world.addEntity(playerCurrencyLabel);
        world.addEntity(playerMenuBackground);
        world.addEntity(playerCurrencyIcon);
        world.addEntity(playerHealthIcon);
    }

    @Override
    public void dispose(World world) {
        //world.removeEntity(player);
        world.removeEntity(playerHealthLabel);
        world.removeEntity(playerCurrencyLabel);
        world.removeEntity(playerMenuBackground);
        world.removeEntity(playerCurrencyIcon);
        world.removeEntity(playerHealthIcon);
    }

}
