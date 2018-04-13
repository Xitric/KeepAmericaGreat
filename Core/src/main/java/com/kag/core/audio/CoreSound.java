package com.kag.core.audio;

import com.badlogic.gdx.audio.Sound;
import com.kag.common.data.ISound;

/**
 * @author Kasper
 */
public class CoreSound implements ISound {

	private Sound sound;

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
