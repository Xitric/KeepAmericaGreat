/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.core.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kag.common.data.GameData;
import com.kag.common.data.World;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.entities.parts.gui.IconPart;
import com.kag.common.spinterfaces.IComponentLoader;
import com.kag.common.spinterfaces.IEntitySystem;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 *
 * @author niels
 */
@ServiceProviders(value = {
	@ServiceProvider(service = IEntitySystem.class)
	,
	@ServiceProvider(service = IComponentLoader.class)
})
public class IconSystem implements IEntitySystem, IComponentLoader {

    private static final Family family = Family.forAll(IconPart.class, PositionPart.class);

    private SpriteBatch sb;
    private AssetManager assetManager;

    @Override
    public void load(World world) {
        sb = new SpriteBatch();
        assetManager = Lookup.getDefault().lookup(AssetManager.class);
    }

    @Override
    public void dispose(World world) {
        sb.dispose();
    }

    @Override
    public void update(float delta, Entity entity, World world, GameData gameData) {
        IconPart iconPart = entity.getPart(IconPart.class);
        PositionPart position = entity.getPart(PositionPart.class);

        Texture texture = assetManager.getResource(iconPart.getAsset());
        
        sb.begin();
        sb.draw(texture, position.getX(), position.getY());
        sb.end();
    }

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public Family getFamily() {
        return family;
    }
}
