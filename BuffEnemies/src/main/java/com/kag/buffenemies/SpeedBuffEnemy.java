/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.buffenemies;

import com.kag.common.entities.Entity;
import com.kag.commonenemy.spinterfaces.IEnemy;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author daniel
 */
@ServiceProvider(service = IEnemy.class)
public class SpeedBuffEnemy implements IEnemy{

    @Override
    public int getDifficulty() {
        return 12;
    }

    @Override
    public Entity create() {
        return BuffEnemyFactory.create(6,100,25,100,150,15);
    }
    
}
