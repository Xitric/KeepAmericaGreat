package com.kag.core.audio;

import com.badlogic.gdx.audio.Sound;
import com.kag.commonaudio.spinterfaces.ISound;

/**
 * Implementation of the {@link ISound} interface for wrapping a LibGDX {@link Sound} object and forwarding all calls to
 * it. This allows for clients to use LibGDX sounds without having a dependency on LibGDX.
 */
public class CoreSound implements ISound {

	private final Sound sound;

	public CoreSound(Sound sound) {
		this.sound = sound;
	}

	@Override
	public void play() {
		sound.play();
	}

	@Override
	public void stop() {
		sound.stop();
	}

	@Override
	public void loop() {
		sound.loop();
	}

	@Override
	public void dispose() {
		sound.dispose();
	}
}
