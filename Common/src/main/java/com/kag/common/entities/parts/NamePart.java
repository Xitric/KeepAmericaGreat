package com.kag.common.entities.parts;

import com.kag.common.entities.IPart;

/**
 * A part describing the name of an entity, which is simply a string.
 */
public class NamePart implements IPart {

	private String name;

	/**
	 * Constructs a new name part from the specified name.
	 *
	 * @param name the name to be wrapped by this part
	 */
	public NamePart(String name) {
		this.name = name;
	}

	/**
	 * Get the name represented by this part.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name to be represented by this part.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
}
