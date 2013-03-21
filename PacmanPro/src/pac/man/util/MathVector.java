package pac.man.util;

public class MathVector {
	public double x;
	public double y;

	public MathVector() {
		this(0.0, 0.0);
	}
	
	public MathVector(double x, double y) {
		set(x, y);
	}

	public MathVector(final MathVector vec) {
		this(vec.x, vec.y);
	}
	
	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void set(MathVector vec) {
		set(vec.x, vec.y);
	}

	public MathVector add(final MathVector vec) {
		x += vec.x;
		y += vec.y;

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
	public double distanceTo(final MathVector vec) {
		return Math.sqrt(distanceSquaredTo(vec));
	}

	// XXX : this belongs to the Point class
	public double distanceSquaredTo(final MathVector vec) {
		final double dx = vec.x - x;
		final double dy = vec.y - y;

		return dx * dx + dy * dy;
	}

	@Override
	public String toString() {
		return String.format("[%.3f, %.3f]", x, y);
	}

	public int getAzimuth() {
		// Y axis values in the screen coordinate system grows downwards
		// (opposite to the Cartesian coordinate system).
		return (int) (360 + Math.round(Math.toDegrees(Math.atan2(-y, x))) + 90) % 360;
	}
}
