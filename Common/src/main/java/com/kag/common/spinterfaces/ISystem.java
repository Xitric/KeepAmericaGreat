/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.common.spinterfaces;

import com.kag.common.data.World;

/**
 *
 * @author andre
 */
public interface ISystem extends IPrioritizable {

	void update(float dt, World world);
}
