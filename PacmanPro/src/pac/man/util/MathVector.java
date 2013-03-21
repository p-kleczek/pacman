package pac.man.util;

public class MathVector {
	public double x;
	public double y;

	public MathVector(final double x, final double y) {
		this.x = x;
		this.y = y;
	}

	public MathVector(final MathVector vec) {
		this.x = vec.x;
		this.y = vec.y;
	}

	public MathVector add(final MathVector another) {
		x += another.x;
		y += another.y;
		
		return this;
	}

	public MathVector scale(final double k) {
		x = x * k;
		y = y * k;

		return this;
	}

	public MathVector rotate(final double angle) {
		final double angleInRadians = Math.toRadians(angle);

		final double xp = x * Math.cos(angleInRadians) - y
				* Math.sin(angleInRadians);
		final double yp = x * Math.sin(angleInRadians) + y
				* Math.cos(angleInRadians);

		x = xp;
		y = yp;

		return this;
	}

	public double length() {
		return Math.sqrt(x * x + y * y);
	}

	public MathVector normalize() {
		final double len = length();
		x /= len;
		y /= len;

		return this;
	}

	// XXX : this belongs to the Point class 
	public double distanceTo(final MathVector v) {
		return Math.sqrt(distanceSquaredTo(v));
	}

	// XXX : this belongs to the Point class 
	public double distanceSquaredTo(final MathVector v) {
		final double dx = v.x - x;
		final double dy = v.y - y;

		return dx * dx + dy * dy;
	}
	
}
