package com.kag.player;

import com.kag.common.data.World;
import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.BoundingBoxPart;
import com.kag.common.entities.parts.CurrencyPart;
import com.kag.common.entities.parts.LifePart;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.spinterfaces.IComponentLoader;

import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IComponentLoader.class)

public class PlayerPlugin implements IComponentLoader {

    private Entity player;
    private Entity playerHealthLabel;
    private Entity playerCurrencyLabel;

    @Override
    public void load(World world) {
        player = new Entity();
        playerHealthLabel = new Entity();
        playerCurrencyLabel = new Entity();

        LifePart lifePart = new LifePart(50);
        CurrencyPart currencyPart = new CurrencyPart(100);

        player.addPart(new PositionPart(100, 100));
        player.addPart(new BoundingBoxPart(0, 0));
        player.addPart(lifePart);
        player.addPart(currencyPart);

        playerHealthLabel.addPart(new PositionPart(50, 50));
        playerHealthLabel.addPart(lifePart);

        playerCurrencyLabel.addPart(currencyPart);

        world.addEntity(player);
        world.addEntity(playerHealthLabel);
        world.addEntity(playerCurrencyLabel);
    }

    @Override
    public void dispose(World world) {
        world.removeEntity(player);
        world.removeEntity(playerHealthLabel);
        world.removeEntity(playerCurrencyLabel);
    }

}
