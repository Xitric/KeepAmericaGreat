package com.kag.common.entities;

import java.util.BitSet;

/**
 * A family is a representation of a collection of {@link IPart parts}. Families can be used by
 * {@link com.kag.common.spinterfaces.IEntitySystem entity systems} to specify what components an entity must have to be
 * processed by that system. This class is heavily inspired by the entity component framework called Ashley.
 */
public class Family {

	/**
	 * Bit set describing a collection of parts of which all must be present.
	 */
	private final BitSet allBits;

	/**
	 * Bit set describing a collection of parts of which at least one must be present.
	 */
	private final BitSet anyBits;

	/**
	 * Bit set describing a collection of parts that must not be present.
	 */
	private final BitSet excludeBits;

	private Family(BitSet allBits, BitSet anyBits, BitSet excludeBits) {
		this.allBits = allBits;
		this.anyBits = anyBits;
		this.excludeBits = excludeBits;
	}

	/**
	 * Create a new family that matches only with collections that contain all the specified parts. Each part must be
	 * specified by its class. This method can be used in the following way:
	 * <p>
	 * {@code Family.forAll(XPart.class, YPart.class);}
	 *
	 * @param partClasses the classes of the parts that must be present to match with this family
	 * @return the resulting family
	 */
	@SafeVarargs
	public static Family forAll(Class<? extends IPart>... partClasses) {
		BitSet allBits = new BitSet();

		for (Class<? extends IPart> partClass : partClasses) {
			allBits.set(PartType.getType(partClass).getId());
		}

		return new Family(allBits, new BitSet(), new BitSet());
	}

	/**
	 * Create a new family that matches only with collections that contain at least one of the specified parts. Each
	 * part must be specified by its class. This method can be used in the following way:
	 * <p>
	 * {@code Family.forAny(XPart.class, YPart.class);}
	 *
	 * @param partClasses the classes of the parts of which at least one must be present to match with this family
	 * @return the resulting family
	 */
	@SafeVarargs
	public static Family forAny(Class<? extends IPart>... partClasses) {
		BitSet anyBits = new BitSet();

		for (Class<? extends IPart> partClass : partClasses) {
			anyBits.set(PartType.getType(partClass).getId());
		}

		return new Family(new BitSet(), anyBits, new BitSet());
	}

	/**
	 * Create a new family that matches only with collections that contain none of the specified parts. Each part must
	 * be specified by its class. This method can be used in the following way:
	 * <p>
	 * {@code Family.forNone(XPart.class, YPart.class);}
	 *
	 * @param partClasses the classes of the parts of which none must be present to match with this family
	 * @return the resulting family
	 */
	@SafeVarargs
	public static Family forNone(Class<? extends IPart>... partClasses) {
		BitSet excludeBits = new BitSet();

		for (Class<? extends IPart> partClass : partClasses) {
			excludeBits.set(PartType.getType(partClass).getId());
		}

		return new Family(new BitSet(), new BitSet(), excludeBits);
	}

	/**
	 * Extend this family to require more parts. All of the specified parts, including the parts initially required by
	 * this family, must be present to match with the resulting family. This method will not affect the existing family,
	 * but instead create a new family that describes the new part requirements.
	 *
	 * @param partClasses the classes of the parts that must be present to match with this family
	 * @return the resulting family
	 */
	@SafeVarargs
	public final Family includingAll(Class<? extends IPart>... partClasses) {
		BitSet newAllBits = new BitSet();
		newAllBits.or(this.allBits);

		for (Class<? extends IPart> partClass : partClasses) {
			newAllBits.set(PartType.getType(partClass).getId());
		}

		return new Family(newAllBits, this.anyBits, this.excludeBits);
	}

	/**
	 * Extend this family to allow more parts. At least one of the specified parts, or one of the parts initially
	 * allowed by this family, must be present to match with the resulting family. This method will not affect the
	 * existing family, but instead create a new family that describes the new part requirements.
	 *
	 * @param partClasses the classes of the parts of which at least one must be present to match with this family
	 * @return the resulting family
	 */
	@SafeVarargs
	public final Family includingAny(Class<? extends IPart>... partClasses) {
		BitSet newAnyBits = new BitSet();
		newAnyBits.or(this.anyBits);

		for (Class<? extends IPart> partClass : partClasses) {
			newAnyBits.set(PartType.getType(partClass).getId());
		}

		return new Family(this.allBits, newAnyBits, this.excludeBits);
	}

	/**
	 * Extend this family to disallow more parts. None of the specified parts, including all of the parts initially
	 * disallowed by this family, may be present to match with the resulting family. This method will not affect the
	 * existing family, but instead create a new family that describes the new part requirements.
	 *
	 * @param partClasses the classes of the parts of which none must be present to match with this family
	 * @return the resulting family
	 */
	@SafeVarargs
	public final Family excluding(Class<? extends IPart>... partClasses) {
		BitSet newExcludingBits = new BitSet();
		newExcludingBits.or(this.excludeBits);

		for (Class<? extends IPart> partClass : partClasses) {
			newExcludingBits.set(PartType.getType(partClass).getId());
		}

		return new Family(this.allBits, this.anyBits, newExcludingBits);
	}

	/**
	 * Test if the specified bit set is matched by this family. For a bit set to be matched it must include all the
	 * parts required by this family, include at least one of the parts specified in {@link Family#forAny(Class[])}
	 * or {@link Family#includingAny(Class[])}, and not contain any of the parts disallowed by this family.
	 * <p>
	 * This operation runs in nearly O(1) complexity, allowing for quick comparisons between system families and parts
	 * of entities.
	 *
	 * @param bits the bit set to test
	 * @return true if the bit set is matched by this family, false otherwise
	 */
	public boolean matches(BitSet bits) {
		//To ensure that the bit set includes all required parts, test if the family bits are a subset of the bit set
		BitSet subsetBits = new BitSet();
		subsetBits.or(allBits);
		subsetBits.and(bits); //If the family is a subset, this will not change subsetBits

		if (!subsetBits.equals(allBits)) return false;

		//To ensure that the bit set excludes all disallowed parts, test if it has no bits in common with the family
		BitSet noneBits = new BitSet();
		noneBits.or(excludeBits);
		noneBits.and(bits);

		if (! noneBits.isEmpty()) return false; //At least one bit was in common

		//If anyBits is empty, then it specifies no extra requirements
		if (anyBits.isEmpty()) return true;

		//To ensure that the bit set includes at least one of the allowed parts, test if there are any matching bits
		BitSet allowBits = new BitSet();
		allowBits.or(anyBits);
		allowBits.and(bits);

		return !allowBits.isEmpty(); //True if at least one bit matched, false otherwise
	}
}
