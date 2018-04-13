package com.kag.common.spinterfaces;

import com.kag.common.data.ISound;

import java.io.InputStream;

/**
 * Interface describing a service for loading sounds into the game. This interface describes a framework independent way
 * of handling sounds. It is not possible, however, to change the sound implementation during runtime.
 * <p>
 * All implementations of this interface guarantee to support the following audio formats:
 * <ul>
 *     <li>MP3</li>
 *     <li>OGG</li>
 *     <li>WAV</li>
 * </ul>
 *
 * @author Kasper
 */
public interface IAudioManager {

	/**
	 * Load the sound file from the specified input stream into memory in a format specific to the current audio
	 * framework. The extension must match the file represented by the specified input stream, to inform the audio
	 * framework of the sound format. When the sound is no longer needed, it must be disposed.
	 * <ul>
	 * <li>Pre-conditions: The audio environment must be initialized</li>
	 * <li>Post-conditions: The audio is in memory and ready for playing</li>
	 * </ul>
	 *
	 * @param input a stream of bytes from the resource to load
	 * @param extension the extension of the audio file to load, such as '{@code wav}' or '{@code mp3}'
	 * @return the reference to the new sound or null if the sound could not be loaded
	 */
	ISound loadSound(InputStream input, String extension);
}
