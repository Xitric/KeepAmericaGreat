/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.common.entities;

import java.util.HashMap;
import java.util.Map;

public class Entity {

    private Map<Class, IPart> parts;

    public Entity() {
	this.parts = new HashMap<Class, IPart>() ;
    }

    public boolean addPart(IPart part) {
	return parts.put(part.getClass(), part) != null;
    }

    public boolean removePart(IPart part) {
	return parts.remove(part.getClass()) != null;
    }

    public boolean removePart(Class partClass) {
	return parts.remove(partClass) != null;
    }

    public IPart getPart(Class partClass) {
	return parts.get(partClass);
    }
}
