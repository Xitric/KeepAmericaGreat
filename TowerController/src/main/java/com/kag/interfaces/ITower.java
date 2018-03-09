/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.interfaces;

import com.kag.common.data.Asset;
import com.kag.common.entities.Entity;

/**
 *
 * @author mstruntze
 */
public interface ITower {
    Asset getAsset();
    Entity create();
}
