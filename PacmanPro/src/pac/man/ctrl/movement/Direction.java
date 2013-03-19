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
		for (Direction d : Direction.values()) {
			if (d.getAngle() == angle)
				return d;
		}

		throw new IllegalArgumentException(String.valueOf(angle));
	}
}
