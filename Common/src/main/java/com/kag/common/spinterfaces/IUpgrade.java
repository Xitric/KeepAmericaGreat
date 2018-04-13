package com.kag.common.spinterfaces;

import com.kag.common.entities.Entity;

public interface IUpgrade {
    boolean isTowerCompatible(Entity entity);
    void upgrade(Entity entity);
}