/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.common.interfaces;

/**
 *
 * @author andre
 */
public interface ISystem {
    void update(float dt, World world);
    int getPriority();    
}
