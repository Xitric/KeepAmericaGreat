/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.commontower.spinterfaces;

import com.kag.common.entities.Entity;
import com.kag.commonasset.spinterfaces.IAsset;

/**
 *
 * @author mstruntze
 */
public interface ITower {
    IAsset getAsset();
    Entity create();
    IAsset getProjectileAsset();
    int getCost();
}
