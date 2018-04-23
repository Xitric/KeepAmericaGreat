package com.kag.commonmath;

/**
 * An immutable representation of the mathematical two-dimensional vector.
 */
public class Vector2f {
	
	/**
	 * The zero vector.
	 */
	public static final Vector2f ZERO = new Vector2f(0.0f, 0.0f);
	
	/**
	 * An axis.
	 */
	public static final Vector2f
		AXIS_X = new Vector2f(1.0f, 0.0f),
		AXIS_Y  = new Vector2f(0.0f, 1.0f);

	public final float x, y;
	
	/**
	 * Constructs a new zero vector.
	 */
	public Vector2f() {
		this(0.0f, 0.0f);
	}
	
	/**
	 * Constructs a new vector with its content equal to that of another
	 * vector.
	 * 
	 * @param other the vector to copy
	 */
	public Vector2f(Vector2f other) {
		this(other.x, other.y);
	}
	
	/**
	 * Constructs a new vector with initial values.
	 * 
	 * @param x the x-component
	 * @param y the y-component
	 */
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Get the length of this vector. For optimization purposes, consider using
	 * {@link #lengthSquared()} instead.
	 *
	 * @return the length of this vector
	 */
	public float length() {
		return (float) Math.sqrt(lengthSquared());
	}

	/**
	 * Get the squared length of this vector.
	 *
	 * @return the squared length of this vector
	 */
	public float lengthSquared() {
		return this.x * this.x + this.y * this.y;
	}

	/**
	 * Get the negated version of this vector. That is, invert the sign of all
	 * coordinates.
	 *
	 * @return the negated version of this vector
	 */
	public Vector2f negate() {
		return new Vector2f(- this.x, - this.y);
	}

	/**
	 * Get the normalized version of this vector.
	 *
	 * @return the normalized version of this vector
	 * @throws IllegalStateException If the vector has a length of zero
	 */
	public Vector2f normalize() {
		float len = length();

		if (len != 0.0f) {
			return this.scale(1.0f / length());
		}

		//Length is zero
		throw new IllegalStateException("Cannot normalize zero length vector");
	}

	/**
	 * Test whether or not this is a unit vector. That is, whether its length
	 * or squared length is very close to one. This function allows for a
	 * deviation of {@code 0.000001} units to take into account floating point
	 * errors.
	 *
	 * @return {@code true} if it is a unit vector, {@code false} otherwise
	 */
	public boolean isUnit() {
		float lenSq = lengthSquared();
		return lenSq > 0.999999f && lenSq < 1.000001f;
	}

	/**
	 * Test whether or not this is a zero vector. That is, whether its length
	 * or squared length is very close to zero. This function allows for a
	 * deviation of {@code 0.000001} units to take into account floating point
	 * errors.
	 *
	 * @return {@code true} if it is a zero vector, {@code false} otherwise
	 */
	public boolean isZero() {
		float lenSq = lengthSquared();
		return lenSq < 0.000001f;
	}

	/**
	 * Get a version of this vector that has been scaled by a given magnitude.
	 *
	 * @param scale the magnitude to scale the vector by
	 * @return a scaled version of this vector
	 */
	public Vector2f scale(float scale) {
		return new Vector2f(this.x * scale, this.y * scale);
	}
	
	/**
	 * Add another vector to this vector. A new vector is returned.
	 * 
	 * @param other the vector to add
	 * @return the result of the addition as a new vector
	 */
	public Vector2f add(Vector2f other) {
		return new Vector2f(this.x + other.x, this.y + other.y);
	}
	
	/**
	 * Subtract another vector from this vector. A new vector is returned.
	 * 
	 * @param other the vector to subtract
	 * @return the result of the subtraction as a new vector
	 */
	public Vector2f sub(Vector2f other) {
		return new Vector2f(this.x - other.x, this.y - other.y);
	}
	
	/**
	 * Get the dot product between this vector and another vector.
	 * 
	 * @param other the vector to dot with
	 * @return the result of the dot product
	 */
	public float dot(Vector2f other) {
		return this.x * other.x +
				this.y * other.y;
	}
	
	/**
	 * Get the determinant between this vector and another vector. The order is
	 * {@code det(this, other)}.
	 * 
	 * @param other the other vector
	 * @return the scalar result of the operation
	 */
	public float det(Vector2f other) {
		return this.x * other.y - other.x * this.y;
	}
	
	/**
	 * Get the result of rotating this vector 90 degrees ccw around the z-axis
	 * in a left-handed coordinate system.
	 * 
	 * @return the result of rotating this vector 90 degrees ccw around the
	 * z-axis in a left-handed coordinate system
	 */
	public Vector2f getLeftHandCCW() {
		return new Vector2f(this.y, - this.x);
	}
	
	/**
	 * Get the result of rotating this vector 90 degrees ccw around the z-axis
	 * in a right-handed coordinate system.
	 * 
	 * @return the result of rotating this vector 90 degrees ccw around the
	 * z-axis in a right-handed coordinate system
	 */
	public Vector2f getRightHandCCW() {
		return new Vector2f(- this.y, this.x);
	}
	
	@Override
	public String toString() {
		return "Vector2f: (" +
				x + ", " +
				y + ")";
	}
}
