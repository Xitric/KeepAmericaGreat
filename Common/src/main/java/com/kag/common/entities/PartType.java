package com.kag.common.entities;

import java.util.HashMap;
import java.util.Map;

/**
 * A part type is a unique integer representation of an {@link IPart}. Each
 * entity part is assigned a unique id, and this id is used as the location of
 * the part's bit in a bit vector representation. This class provides a static
 * method for querying the type of a part class, which automatically registers
 * new parts.
 *
 * @author Kasper
 */
public class PartType {

	private static final Map<Class<? extends IPart>, PartType> partTypes = new HashMap<>();
	private static int nextId;

	private final int id;

	/**
	 * Get the part type associated with the specified part class. If the part
	 * class is unknown, a new part type with a unique is is generated.
	 * Otherwise the existing part type is guaranteed to be returned.
	 *
	 * @param partClass the class of the part to get the type for
	 * @return the type for the specified part class, storing the part's unique id
	 */
	static PartType getType(Class<? extends IPart> partClass) {
		return partTypes.computeIfAbsent(partClass, k -> new PartType());
	}

	private PartType() {
		id = nextId++;
	}

	int getId() {
		return id;
	}
}
