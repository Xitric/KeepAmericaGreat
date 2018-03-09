/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.common.spinterfaces;

import com.kag.common.entities.Entity;

/**
 *
 * @author andre
 */
public interface ICollision {
    boolean doesCollide(Entity a, Entity b);
    
}
