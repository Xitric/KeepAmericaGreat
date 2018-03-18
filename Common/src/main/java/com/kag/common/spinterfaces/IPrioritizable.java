/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.common.spinterfaces;

/**
 * Interface describing an element that can be ordered by its priority. The
 * lower the priority number, the higher the object's priority is, and the
 * sooner it will be considered. This means that objects of this interface
 * should be sorted in increasing order if processed from the beginning of the
 * collection.
 *
 * @author Emil
 */
public interface IPrioritizable {

	/**
	 * Get the priority of this object. Lower values indicate a higher priority.
	 *
	 * @return the priority of this object
	 */
	int getPriority();
}
