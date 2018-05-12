package com.kag.common.spinterfaces;

/**
 * Interface describing an element that can be ordered by its priority. The
 * lower the priority number, the higher the object's priority is, and the
 * sooner it will be considered. This means that objects of this interface
 * should be sorted in increasing order if processed from the beginning of the
 * collection.
 */
public interface IPrioritizable {

	int PRE_UPDATE = 0,
			UPDATE_PASS_1 = 10,
			UPDATE_PASS_2 = 20,
			UPDATE_PASS_3 = 30,
			POST_UPDATE = 40,
			PRE_RENDER = 50,
			RENDER_PASS = 60,
			POST_RENDER = 70;

	/**
	 * Get the priority of this object. Lower values indicate a higher priority.
	 *
	 * @return the priority of this object
	 */
	int getPriority();
}
