/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.enemybuffcontroller;

import com.kag.common.data.GameData;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.BoundingBoxPart;
import com.kag.common.entities.parts.MovingPart;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.map.World;
import com.kag.common.spinterfaces.IEntitySystem;
import com.kag.common.spinterfaces.IPrioritizable;
import com.kag.commonenemy.entities.parts.EnemyPart;
import com.kag.commonenemybuff.BuffPart;
import com.kag.commonenemybuff.LifeBuffPart;
import com.kag.commonenemybuff.SpeedBuffPart;
import com.kag.commonenemywalking.entities.parts.WalkingPart;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author danie
 */
@ServiceProvider(service = IEntitySystem.class)
public class EnemyBuffSystem implements IEntitySystem {

    private static final Family FAMILY = Family.forAny(SpeedBuffPart.class, LifeBuffPart.class);
    private static final Family ENEMY_FAMILY = Family.forAll(EnemyPart.class, MovingPart.class, PositionPart.class, BoundingBoxPart.class);

    @Override
    public Family getFamily() {
        return FAMILY;
    }

    @Override
    public void update(float delta, Entity entity, World world, GameData gameData) {
        List<Entity> entities = world.getEntitiesByFamily(ENEMY_FAMILY);
        for (Entity entityToBuff : entities) {
            if (inRange(entity, entityToBuff)) {
                buffEntity(entityToBuff, entity, delta);
            }
        }
    }

    @Override
    public int getPriority() {
        return IPrioritizable.UPDATE_PASS_1;
    }

    private boolean inRange(Entity entity, Entity entityToBuff) {
        if (entity == null || entityToBuff == null) {
            return false;
        }

        PositionPart posPart1 = entity.getPart(PositionPart.class);
        PositionPart posPart2 = entityToBuff.getPart(PositionPart.class);
        BoundingBoxPart boundingBoxPart = entityToBuff.getPart(BoundingBoxPart.class);
        BuffPart buffPart = ((List<BuffPart>) entity.getPartsDeep(BuffPart.class)).get(0);
        List<BuffPart> buffPart2 = (List) entityToBuff.getPartsDeep(BuffPart.class);
        if (posPart1 == null || posPart2 == null || boundingBoxPart == null || buffPart == null || !buffPart2.isEmpty()) {
            return false;
        }
        //Quick maths
        float distX = posPart1.getX() - posPart2.getX();
        float distY = posPart1.getY() - posPart2.getY();
        float dist = (float) Math.sqrt(distX * distX + distY * distY);
        if (dist < buffPart.getBuffRadius() + (boundingBoxPart.getWidth() / 2)) {
            return true;
        }
        return false;
    }

    private void buffEntity(Entity entityToBuff, Entity entity, float delta) {
        BuffPart buffPart = ((List<BuffPart>) entity.getPartsDeep(BuffPart.class)).get(0);
        buffPart.buff(entityToBuff, delta);
    }

}
