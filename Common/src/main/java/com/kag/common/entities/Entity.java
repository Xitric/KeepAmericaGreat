/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.common.entities;

/**
 *
 * @author Sofie Jørgensen
 */
public class Entity {

    public boolean addPart(IPart part) {
        return false;
    }

    public boolean removePart(IPart part) {
        return false;
    }

    public boolean removePart(Class partClass) {
        return false;
    }

    public IPart getPart(Class partClass) {
        return null;
    }
}
