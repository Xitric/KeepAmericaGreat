package com.kag.common.spinterfaces;

import com.kag.common.data.ISound;

import java.io.InputStream;

/**
 * @author Kasper
 */
public interface IAudioManager {

	ISound loadSound(InputStream input, String extension);
}
