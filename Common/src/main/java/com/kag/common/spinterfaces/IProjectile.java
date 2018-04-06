package com.kag.common.spinterfaces;

import com.kag.common.data.World;
import com.kag.common.entities.Entity;

public interface IProjectile {
    void createProjectile(World world, Entity entity, int damage, float movingSpeed, float rotation);
}
