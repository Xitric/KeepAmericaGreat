package com.kag.core.audio;

import com.badlogic.gdx.files.FileHandle;

import java.io.InputStream;

/**
 * @author Kasper
 */
public class InputStreamFileHandle extends FileHandle {

	private final InputStream stream;
	private final String extension;

	public InputStreamFileHandle(InputStream stream, String extension) {
		this.stream = stream;
		this.extension = extension;
	}

	@Override
	public InputStream read() {
		return stream;
	}

	@Override
	public String extension() {
		return extension;
	}
}
