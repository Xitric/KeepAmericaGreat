package com.kag.common.entities;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class Entity {

	private final Map<Class<? extends IPart>, IPart> parts;
	private final BitSet bits;

	public Entity() {
		this.parts = new HashMap<>();
		bits = new BitSet();
	}

	public void addPart(IPart part) {
		parts.put(part.getClass(), part);
		
		PartType type = PartType.getType(part.getClass());
		bits.set(type.getId());
	}

	public boolean removePart(IPart part) {
		return removePart(part.getClass());
	}

	public boolean removePart(Class partClass) {
		PartType type = PartType.getType(partClass);
		bits.set(type.getId(), false);
		
		return parts.put(partClass, null) != null;
	}

	public <T extends IPart> T getPart(Class<T> partClass) {
		return (T) parts.get(partClass);
	}
	
	public BitSet getBits() {
		return bits;
	}
}
