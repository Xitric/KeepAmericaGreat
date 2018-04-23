package com.kag.common.entities.parts;

import com.kag.common.entities.IPart;

/**
 * A part used by entities that must signal that they occupy the tile in the world that they are standing on. Other
 * entities that require a tile for themselves can use this information to determine if a tile is free or not. Entities
 * that can share tiles with other entities can simply ignore the presence of this part in other entities.
 */
public class BlockingPart implements IPart {

}
