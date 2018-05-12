package com.kag.core.game;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.openide.modules.ModuleInstall;
import org.openide.util.Lookup;

public class Installer extends ModuleInstall {

	@Override
	public void restored() {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Keep America Great";
//        cfg.fullscreen = true;
		cfg.width = 960;
		cfg.height = 640;
		cfg.useGL30 = false;
		cfg.resizable = false;

		new LwjglApplication(Lookup.getDefault().lookup(Game.class), cfg);
	}
}
