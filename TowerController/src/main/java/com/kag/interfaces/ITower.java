/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.interfaces;

import com.kag.common.data.IAsset;
import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.AssetPart;

/**
 *
 * @author mstruntze
 */
public interface ITower {
    IAsset getAsset();
    Entity create();
}
