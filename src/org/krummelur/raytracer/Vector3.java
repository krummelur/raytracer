package org.krummelur.raytracer;

public class Vector3 {
	double x,y,z;

	public static Vector3 ZERO() {
		return new Vector3();
	}

	private Vector3() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3 add(Vector3 other) {
		return new Vector3(this.x+other.x, this.y+other.y, this.z+other.z);
	}
	
	public Vector3 subtract(Vector3 other) {
		return new Vector3(this.x-other.x, this.y-other.y, this.z-other.z);
	}

	public double distance(Vector3 other) {
		return Math.sqrt(distanceSquared(other));
	}

	public double distanceSquared(Vector3 other) {
		Vector3 delta = subtract(other);
		double edge1Sqrd = (delta.y*delta.y) + (delta.z*delta.z);
		return (delta.x*delta.x) + edge1Sqrd;
		//return edge1Sqrd + edge2Sqrd;
	}

	public double dot(Vector3 other) {
		return this.x*other.x + this.y*other.y + this.z*other.z;
	}

	boolean equals(Vector3 other) {
		return this.x == other.x && this.y == other.y && this.z == other.z;
	}

	public Vector3 divide(double d) {
		return new Vector3(this.x/d, this.y/d, this.z/d);
	}

	public Vector3 normalize() {
		return divide(distance(Vector3.ZERO()));
	}

	@Override
	public String toString() {
		return "{" + x+ ", " + ", " + y + ", " + ", " + z +"}";
	}

	public Vector3 multiply(Vector3 other) {
	    return new Vector3(this.x*other.x, this.y*other.y, this.z*other.z);
    }

	Vector3 cross(Vector3 other) {
		return new Vector3(
				this.y*other.z - this.z*other.y,
				this.z*other.x - this.x*other.z,
				this.x*other.y - this.y*other.x);
	}

    public Vector3 multiply(double d) {
        return new Vector3(this.x*d, this.y*d, this.z*d);
	}

	public Vector3 clampNegative() {
	    return new Vector3(Math.max(this.x, 0), Math.max(this.y, 0), Math.max(this.z, 0));
    }
}