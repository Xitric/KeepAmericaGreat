package com.kag.common.entities;

import java.util.HashMap;
import java.util.Map;

public class Entity {

	private final Map<Class, IPart> parts;

	public Entity() {
		this.parts = new HashMap<>();
	}

	public void addPart(IPart part) {
		parts.put(part.getClass(), part);
	}

	public boolean removePart(IPart part) {
		return parts.remove(part.getClass()) != null;
	}

	public boolean removePart(Class partClass) {
		return parts.remove(partClass) != null;
	}

	public <T extends IPart> T getPart(Class<T> partClass) {
		return (T) parts.get(partClass);
	}
}
