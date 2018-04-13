package com.kag.common.data;

/**
 * Class for representing a sound file that can be played in the game. Objects of this class should always be disposed
 * before releasing the reference to the object.
 *
 * @author Kasper
 */
public interface ISound {

	/**
	 * Play this sound once.
	 */
	void play();

	/**
	 * Stop playing this sound. If the sound is not playing, calling this method has no effect.
	 */
	void stop();

	/**
	 * Play this sound continuously. As soon as the sound stops, it begins playing again. Stop the sound by calling
	 * {@link ISound#stop()}.
	 */
	void loop();

	/**
	 * Dispose all resources held by this sound object. Once this method has been called the sound object can no longer
	 * be used.
	 */
	void dispose();
}
