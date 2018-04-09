package com.kag.passivetowers;

import com.kag.common.data.GameData;
import com.kag.common.data.World;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.spinterfaces.IComponentLoader;
import com.kag.common.spinterfaces.IEntitySystem;

public class PassiveTowerControlSystem implements IEntitySystem, IComponentLoader {

    @Override
    public void load(World world) {

    }

    @Override
    public void dispose(World world) {
        for(Entity entity : PassiveTowerFactory.getInstance().getTowersCreated()){
            world.removeEntity(entity);
        }
    }

    @Override
    public Family getFamily() {
        return null;
    }

    @Override
    public void update(float delta, Entity entity, World world, GameData gameData) {

    }

    @Override
    public int getPriority() {
        return 0;
    }
}
