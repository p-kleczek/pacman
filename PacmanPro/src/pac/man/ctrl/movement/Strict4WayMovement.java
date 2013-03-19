package pac.man.ctrl.movement;

import pac.man.PacMan;
import pac.man.util.MathVector;

public class Strict4WayMovement extends MovementAlgorithm {
	private final double factor;

	public Strict4WayMovement(double factor) {
		this.factor = factor;
	}

	public Strict4WayMovement() {
		this(1.0);
	}

	@Override
	public MathVector computeSpeed(MathVector position,
			MathVector currentSpeed, MathVector preferredDir) {

		// Similar to Player.handleMove() logic. It's documented there.
		int angle = (int) (360 + Math.round(Math.toDegrees(Math.atan2(
				-preferredDir.y, preferredDir.x))));

		// TODO : sprawdzic i uproscic
		int index = (angle + 45) % 360;

		MathVector vec = Direction.getDirectionByAngle(index).getShift();
		return vec.scale(factor * PacMan.getSpeed().getGain());
	}
}