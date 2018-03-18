package com.kag.common.entities;

import java.util.BitSet;

/**
 *
 * @author Kasper
 */
public class Family {
	
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
	
	public BitSet getBits() {
		return bits;
	}
}
