package com.kag.common.entities;

import java.util.*;

/**
 * An entity is a combination of {@link IPart parts} that can exist in the game
 * world. Entities are automatically processed by all systems that match its
 * family of parts. An entity is allowed to have multiple instances of the same
 * part type, although systems are free to use just those parts they want.
 *
 * @author Kasper
 */
public class Entity {

	private final Map<Class<? extends IPart>, Set<IPart>> parts;
	private final BitSet bits;

	public Entity() {
		this.parts = new HashMap<>();
		bits = new BitSet();
	}

	/**
	 * Add the specified part to this entity. After adding the part, the entity
	 * will automatically be processed by those new systems whose families it
	 * satisfies.
	 *
	 * @param part the part to add
	 */
	public void addPart(IPart part) {
		Set<IPart> partSet = parts.getOrDefault(part.getClass(), new HashSet());
		partSet.add(part);
		parts.put(part.getClass(), partSet);

		PartType type = PartType.getType(part.getClass());
		bits.set(type.getId());
	}

	/**
	 * Remove the specified part from this entity. After removing the part, the
	 * entity will no longer be processed by those systems whose families it
	 * ceases to satisfy.
	 *
	 * @param part the part to remove
	 * @return whether a part was removed as a result of calling this method. This will be false if the entity did not
	 * have the part
	 */
	public boolean removePart(IPart part) {
		PartType type = PartType.getType(part.getClass());
		bits.set(type.getId(), false);

		return getParts(part.getClass()).remove(part);
	}

	/**
	 * Get the first part of a specified type from this entity. If the entity
	 * has multiple parts of this type, an arbitrary part will be returned.
	 *
	 * @param <T>       the type of part to retrieve
	 * @param partClass the class describing what type of part to retrieve
	 * @return the first part of the specified type, or null if the entity has no such parts
	 */
	public <T extends IPart> T getPart(Class<T> partClass) {
		Collection<T> partCollection = getParts(partClass);

		if (partCollection.isEmpty()) {
			return null;
		}

		return partCollection.iterator().next();
	}

	/**
	 * Get all the parts of the specified type from this entity.
	 *
	 * @param <T>       the type of part to retrieve
	 * @param partClass the class describing what type of part to retrieve
	 * @return the parts of the specified type. Will be empty if the entity has no such parts
	 */
	public <T extends IPart> Collection<T> getParts(Class<T> partClass) {
		return (Set<T>) parts.getOrDefault(partClass, new HashSet<>());
	}

	/**
	 * Test if this entity contains at least one part of the specified type.
	 *
	 * @param partClass the type of part to test for
	 * @return true if this entity contains at least one part of the specified type, false otherwise
	 */
	public boolean hasPart(Class<? extends IPart> partClass) {
		return bits.get(PartType.getType(partClass).getId());
	}

	/**
	 * Get the bit set describing the types of parts that this entity has.
	 *
	 * @return the entity's bit set
	 */
	public BitSet getBits() {
		return bits;
	}
}
