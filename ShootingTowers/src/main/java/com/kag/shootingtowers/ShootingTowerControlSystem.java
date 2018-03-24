package com.kag.shootingtowers;

import com.kag.common.data.GameData;
import com.kag.common.data.World;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.spinterfaces.IComponentLoader;
import com.kag.common.spinterfaces.IEntitySystem;

public class ShootingTowerControlSystem implements IEntitySystem, IComponentLoader {

    @Override
    public void load(World world) {



    }

    @Override
    public void dispose(World world) {
        for(Entity entity : ShootingTowerFactory.getInstance().getTowersCreated()){
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
