package com.kag.core.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.kag.commonaudio.spinterfaces.IAudioManager;
import com.kag.commonaudio.spinterfaces.ISound;
import org.openide.util.lookup.ServiceProvider;

import java.io.InputStream;

/**
 * @author Kasper
 */
@ServiceProvider(service = IAudioManager.class)
public class AudioManager implements IAudioManager {

	@Override
	public ISound loadSound(InputStream input, String extension) {
		FileHandle fileHandle = new InputStreamFileHandle(input, extension);
		Sound sound = Gdx.audio.newSound(fileHandle);
		return new CoreSound(sound);
	}
}
