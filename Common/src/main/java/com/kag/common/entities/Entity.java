package com.kag.common.entities;

import java.util.*;

/**
 * An entity is a combination of {@link IPart parts} and it can exist in the game
 * world. Entities are automatically processed by all systems that match its
 * family of parts. An entity is allowed to have multiple instances of the same
 * part type, although systems are free to use just those parts they want.
 */
public class Entity {

	private final Map<Class<? extends IPart>, Set<IPart>> parts;
	private final BitSet bits;

	/**
	 * Constructs a new entity with no parts.
	 */
	public Entity() {
		this.parts = new HashMap<>();
		bits = new BitSet();
	}

	/**
	 * Add the specified part to this entity. After adding the part, the entity
	 * will automatically be processed by those systems whose families it now
	 * satisfies.
	 *
	 * @param part the part to add
	 */
	public void addPart(IPart part) {
		Set<IPart> partSet = parts.getOrDefault(part.getClass(), new HashSet<>());
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
		if (getParts(part.getClass()).removeIf(p -> p == part)) {
			if (getParts(part.getClass()).isEmpty()) {
				PartType type = PartType.getType(part.getClass());
				bits.set(type.getId(), false);

				return true;
			}
		}

		return false;
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
	 * Get all the parts of exactly the specified type from this entity. This method does not consider subtypes.
	 *
	 * @param <T>       the type of part to retrieve
	 * @param partClass the class describing what type of part to retrieve
	 * @return the parts of exactly the specified type. Will be empty if the entity has no such parts
	 * @see Entity#getPartsDeep(Class)
	 */
	public <T extends IPart> Collection<T> getParts(Class<T> partClass) {
		return (Set<T>) parts.getOrDefault(partClass, new HashSet<>());
	}

	/**
	 * Get all the parts of the specified type or any of its subtypes from this entity.
	 *
	 * @param <T>        the super type of the parts to retrieve
	 * @param superClass the class describing the super type of the parts to retrieve
	 * @return the parts of the specified type or any of its subtypes. Will be empty if the entity has no such parts
	 * @see Entity#getParts(Class)
	 */
	public <T extends IPart> Collection<? extends T> getPartsDeep(Class<T> superClass) {
		Collection<T> returnParts = new ArrayList<>();

		for (Class<? extends IPart> clazz : parts.keySet()) {
			if (superClass.isAssignableFrom(clazz)) {
				Collection<? extends IPart> parts = getParts(clazz);

				for (IPart c : parts) {
					returnParts.add((T) c);
				}
			}
		}

		return returnParts;
	}

	/**
	 * Test if this entity contains at least one part of the specified type. This method does not consider subtypes.
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
