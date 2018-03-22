package com.kag.mapgenerator;

public enum TileOrientation {
	
	N(0), E(1), S(2), W(3),
	NW(4), NE(5), SW(6), SE(7),
	dNE(8), dNW(9), dSE(10), dSW(11),
	BASE(12);

	private final int spriteIndex;

	TileOrientation(int spriteIndex) {
		this.spriteIndex = spriteIndex;
	}

	public int getSpriteIndex() {
		return spriteIndex;
	}
}
