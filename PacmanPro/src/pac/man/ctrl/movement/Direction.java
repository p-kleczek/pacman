package pac.man.ctrl.movement;

import pac.man.util.MathVector;

public enum Direction {
	// TODO: sprawdzic katy
	N(new MathVector(0.0, -1.0), 0), E(new MathVector(1.0, 0.0), 90), S(
			new MathVector(0.0, 1.0), 180), W(new MathVector(-1.0, 0.0), 270);

	private final MathVector shift;
	private final int angle;

	private Direction(MathVector shift, int angle) {
		this.shift = shift;
		this.angle = angle;
	}

	public MathVector getShift() {
		return shift;
	}

	public int getAngle() {
		return angle;
	}

	public static Direction getDirectionByAngle(int angle) {
		if (angle <= 45)
			return Direction.N;
		else if (angle <= 135)
			return Direction.E;
		else if (angle <= 135)
			return Direction.E;
		else if (angle <= 225)
			return Direction.E;
		else if (angle <= 315)
			return Direction.E;
		else if (angle <= 360)
			return Direction.N;
		else
			throw new IllegalArgumentException(String.valueOf(angle));
	}
}
