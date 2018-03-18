/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.common.spinterfaces;

import com.kag.common.data.World;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;

/**
 *
 * @author andre
 */
public interface IEntitySystem extends IPrioritizable {

	Family getFamily();
	
	void update(float delta, Entity entity, World world);
}
