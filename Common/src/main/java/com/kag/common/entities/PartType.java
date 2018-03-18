package com.kag.common.entities;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Kasper
 */
public class PartType {
	
	private static final Map<Class<? extends IPart>, PartType> partTypes = new HashMap<>();
	private static int nextId;
	
	private final int id;
	
	public static PartType getType(Class<? extends IPart> partClass) {
		PartType type = partTypes.get(partClass);
		
		if (type == null) {
			type = new PartType();
			partTypes.put(partClass, type);
		}
		
		return type;
	}
	
	private PartType() {
		id = nextId ++;
	}
	
	public int getId() {
		return id;
	}
}
