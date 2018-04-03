/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.collision;

import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.BoundingBoxPart;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.spinterfaces.ICollision;
import org.openide.util.lookup.ServiceProvider;


/**
 * @author Sofie Jørgensen
 */
@ServiceProvider(service = ICollision.class)
public class Collision implements ICollision {

	@Override
	public boolean doesCollide(Entity a, Entity b) {
		ensureValid(a, b);

		PositionPart apos = a.getPart(PositionPart.class);
		PositionPart bpos = b.getPart(PositionPart.class);

		BoundingBoxPart abox = a.getPart(BoundingBoxPart.class);
		BoundingBoxPart bbox = b.getPart(BoundingBoxPart.class);

		return apos.getY() + abox.getHeight() / 2 > bpos.getY() - bbox.getHeight() / 2 &&
				apos.getY() - abox.getHeight() / 2 < bpos.getY() + bbox.getHeight() / 2 &&
				apos.getX() + abox.getWidth() / 2 > bpos.getX() - bbox.getWidth() / 2 &&
				apos.getX() - abox.getWidth() / 2 < bpos.getX() + bbox.getWidth() / 2;

	}

	private void ensureValid(Entity a, Entity b) {
		if (! a.hasPart(BoundingBoxPart.class) ||
				! a.hasPart(PositionPart.class) ||
				! b.hasPart(BoundingBoxPart.class) ||
				! b.hasPart(PositionPart.class)) {
			throw new IllegalStateException("Pre condition of collision not satisfied - missing part.");
		}
	}
}
