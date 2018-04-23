package com.kag.common.spinterfaces;

import com.kag.common.map.World;

/**
 * Interface describing a class that can only initialize its internal state after the game engine has been initialized,
 * or which needs to initialize graphical resources, which is only guaranteed to be possible on certain threads such as
 * the game thread. Furthermore, the class needs to dispose its internal state or dispose graphical resources before it
 * is removed from the game.
 * <p>
 * The reason that this cannot simply be done in a module installer, is that it does not provide a reference to the game
 * state such as the game world, and the installer is not guaranteed to run on a thread allowing for the loading or
 * disposal of graphical resources.
 */
public interface IComponentLoader {

	/**
	 * Initialize this object so that it is ready for execution. This is the first method that will be called by the
	 * game engine when a new module is installed. Thus, the game engine guarantees that this method is invoked before
	 * calling methods defined by other interfaces, that this object might implement.
	 * <ul>
	 * <li>Pre-conditions: The game engine must be initialized and it must be possible to initialize graphical resources
	 * on the current thread. No other methods on this object can have been called by the game engine.</li>
	 * <li>Post-conditions: This object is ready for processing. Thus, it must be safe for the game engine to call any
	 * other methods on this object.</li>
	 * </ul>
	 *
	 * @param world the state of the game world
	 */
	void load(World world);

	/**
	 * Release all resources created and managed by this object. The implementing object must ensure that there will be
	 * no further traces of it in the game after this method returns. The game engine guarantees that no further methods
	 * will be invoked on this object once this method returns.
	 * <ul>
	 * <li>Pre-conditions: The game engine must be initialized and it must be possible to dispose graphical resources
	 * on the current thread.</li>
	 * <li>Post-conditions: All resources created by this object have been released, all its entities have been removed
	 * from the world, and it is safe to completely uninstall. There are no more traces that this object was ever
	 * installed. No further methods are called by the game engine on this object.</li>
	 * </ul>
	 *
	 * @param world the state of the game world
	 */
	void dispose(World world);
}
