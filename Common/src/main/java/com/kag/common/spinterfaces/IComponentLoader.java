/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.common.spinterfaces;

import com.kag.common.data.World;

/**
 * Interface describing a class responsible for adding a component to the game
 * when it is loaded, and removing a component when it is unloaded. The reason
 * we cannot simply use an installer is that we must not initialize game
 * specific contents of a component before the game engine has been created and
 * the environment is ready. Furthermore, the load and dispose methods wlll
 * benefit greatly from having references to the game world so that they can
 * change the state of the game data, and this cannot be supplied in an
 * installer.
 *
 * @author Kasper
 */
public interface IComponentLoader {

	/**
	 * Initialize this component so that it is ready for execution. A component
	 * is free to postpone initialization to its systems, as long as it
	 * guarantees that they will be valid for processing.
	 * <ul>
	 * <li>Pre-conditions: The game engine must be initialized and its
	 * environment must be valid, such that any resource can be created</li>
	 * <li>Post-conditions: This component is ready for execution. This means
	 * that its systems are ready to be processed</li>
	 * </ul>
	 *
	 * @param world the state of the game world
	 */
	void load(World world);

	/**
	 * Release all resources created and managed by this component. The
	 * implementing component must ensure that there will be no further traces
	 * of it in the game after this method returns.
	 * <ul>
	 * <li>Pre-conditions: The game engine must be initialized and its
	 * environment must be valid, such that any resource can be removed</li>
	 * <li>Post-conditions: All resources consumed by this component have been
	 * released, all its entities removed from the world, and it is safe to
	 * completely uninstall. All side effects of this component will be reverted
	 * in the game</li>
	 * </ul>
	 *
	 * @param world the state of the game world
	 */
	void dispose(World world);
}
