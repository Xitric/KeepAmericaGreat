package com.kag.common.data;

/**
 * Class used to define a color in the sRGB color space. The alpha value of the
 * color can be set explicitly.
 *
 * @author Kasper
 */
public class Color {

	/** A color. */
	public static final Color
			BLACK = new Color(0, 0, 0),
			WHITE = new Color(255, 255, 255),
			RED = new Color(255, 0, 0),
			GREEN = new Color(0, 255, 0),
			BLUE = new Color(0, 0, 255),
			YELLOW = new Color(255, 255, 0),
			ORANGE = new Color(255, 127, 0),
			PURPLE = new Color(128, 0, 128),
			PINK = new Color(255, 192, 203),
			BROWN = new Color(150, 75, 0),
			GREY = new Color(128, 128, 128),
			CYAN = new Color(0, 255, 255);

	private static final int
			ALPHA_OFFSET = 24,
			RED_OFFSET = 16,
			GREEN_OFFSET = 8,
			BLUE_OFFSET = 0;

	/**
	 * Used to store the color value. The blue component is in bits 1 to 8, the
	 * green component is in bits 9 to 16, the red component is in bits 17 to
	 * 24 and the alpha component is in bits 25 to 32.
	 */
	private int value;

	/**
	 * Construct a new, fully opaque, black color.
	 */
	public Color() {
		this(255, 0, 0, 0);
	}

	/**
	 * Construct a new color from three color components: red, green and blue.
	 * The alpha value is initialized as fully opaque, that is 255.
	 *
	 * @param r the red component in the range 0 - 255
	 * @param g the green component in the range 0 - 255
	 * @param b the blue component in the range 0 - 255
	 * @throws IllegalArgumentException If a component is not in the range
	 */
	public Color(int r, int g, int b) {
		this(255, r, g, b);
	}

	/**
	 * Construct a new color from four color components: alpha, red, green and
	 * blue.
	 *
	 * @param a the alpha component in the range 0 - 255
	 * @param r the red component in the range 0 - 255
	 * @param g the green component in the range 0 - 255
	 * @param b the blue component in the range 0 - 255
	 * @throws IllegalArgumentException If a component is not in the range
	 */
	public Color(int a, int r, int g, int b) {
		set(a, r, g, b);
	}

	/**
	 * Construct a new color from an integer value. The blue component is in
	 * bits 1 to 8, the green component is in bits 9 to 16, the red component
	 * is in bits 17 to 24 and the alpha component is in bits 25 to 32.
	 * <p>
	 * If written as a hexadecimal, the format is {@code 0xAARRGGBB}.
	 *
	 * @param value the color value
	 */
	public Color(int value) {
		this.value = value;
	}

	/**
	 * Construct a new color as a copy of an existing color.
	 *
	 * @param other the color to copy
	 */
	public Color(Color other) {
		this.value = other.getValue();
	}

	/**
	 * Test whether the specified integer color values are in the range 0 -
	 * 255.
	 *
	 * @param a the alpha component to test
	 * @param r the red component to test
	 * @param g the green component to test
	 * @param b the blue component to test
	 * @throws IllegalArgumentException If a component is not in the range
	 */
	private void rangeCheck(int a, int r, int g, int b) {
		StringBuilder error = new StringBuilder("Error, components not in the range [0 - 255]:");
		boolean errorOccured = false;

		if (a < 0 || a > 255) {
			error.append(" Alpha");
			errorOccured = true;
		}
		if (r < 0 || r > 255) {
			error.append(" Red");
			errorOccured = true;
		}
		if (g < 0 || g > 255) {
			error.append(" Green");
			errorOccured = true;
		}
		if (b < 0 || b > 255) {
			error.append(" Blue");
			errorOccured = true;
		}

		if (errorOccured)
			throw new IllegalArgumentException(error.toString());
	}

	/**
	 * Set the color components of this color.
	 *
	 * @param a the alpha component in the range 0 - 255
	 * @param r the red component in the range 0 - 255
	 * @param g the green component in the range 0 - 255
	 * @param b the blue component in the range 0 - 255
	 * @throws IllegalArgumentException If a component is not in the range
	 */
	private void set(int a, int r, int g, int b) {
		rangeCheck(a, r, g, b);

		value = (a << ALPHA_OFFSET) |
				(r << RED_OFFSET) |
				(g << GREEN_OFFSET) |
				(b << BLUE_OFFSET);
	}

	/**
	 * Get the alpha component of this color in the range 0 - 255.
	 *
	 * @return the alpha component
	 */
	public int getAlpha() {
		return (value >> ALPHA_OFFSET) & 0xFF;
	}

	/**
	 * Get the red component of this color in the range 0 - 255.
	 *
	 * @return the red component
	 */
	public int getRed() {
		return (value >> RED_OFFSET) & 0xFF;
	}

	/**
	 * Get the green component of this color in the range 0 - 255.
	 *
	 * @return the green component
	 */
	public int getGreen() {
		return (value >> GREEN_OFFSET) & 0xFF;
	}

	/**
	 * Get the blue component of this color in the range 0 - 255.
	 *
	 * @return the blue component
	 */
	public int getBlue() {
		return (value >> BLUE_OFFSET) & 0xFF;
	}

	/**
	 * Get the integer value representing this color.
	 *
	 * @return the color value as an integer
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Invert this color. A new color is returned.
	 *
	 * @return the inverted color
	 */
	public Color invert() {
		return new Color(255 - this.getRed(), 255 - this.getGreen(), 255 - this.getBlue());
	}

	@Override
	public String toString() {
		return "ColorARGB: (" +
				getAlpha() + ", " +
				getRed() + ", " +
				getGreen() + ", " +
				getBlue() + ")";
	}
}
