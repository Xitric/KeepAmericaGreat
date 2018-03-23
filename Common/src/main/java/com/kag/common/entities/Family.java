package com.kag.common.entities;

import java.util.BitSet;

/**
 * A family is a representation of a collection of {@link IPart parts}. Families
 * can be used by
 * {@link com.kag.common.spinterfaces.IEntitySystem entity systems} to specify
 * what components an entity must have to be processed by that system. This
 * class is heavily inspired by the entity component framework caled Ashley.
 *
 * @author Kasper
 */
public class Family {

	/**
	 * Create a new family from the specified collection of parts. Each part
	 * must be specified by its class. This method can be used in the following
	 * way:
	 * <p>
	 * {@code Family.forAll(XPart.class, YPart.class);}
	 *
	 * @param partClasses the classes of the parts represented by this family
	 * @return the resulting family
	 */
	@SafeVarargs
	public static Family forAll(Class<? extends IPart>... partClasses) {
		BitSet bits = new BitSet();

		for (Class<? extends IPart> partClass : partClasses) {
			bits.set(PartType.getType(partClass).getId());
		}

		return new Family(bits);
	}

	private final BitSet bits;

	private Family(BitSet bits) {
		this.bits = bits;
	}

	/**
	 * Get the bit set representing the parts of this family. Using a bit set
	 * allows for nearly O(1) complexity comparisons between families and parts
	 * of entities.
	 *
	 * @return the bit set representing the parts of this family
	 */
	public BitSet getBits() {
		return bits;
	}
}
