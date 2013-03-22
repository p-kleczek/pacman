package pac.man.ctrl.movement;

import pac.man.PacMan;
import pac.man.util.MathVector;

public class Strict4WayMovement extends MovementAlgorithm {
	private final double factor;
	private final MathVector vec = new MathVector();

	public Strict4WayMovement(double factor) {
		this.factor = factor;
	}

	public Strict4WayMovement() {
		this(1.0);
	}

	@Override
	public MathVector computeSpeed(MathVector position,
			MathVector currentSpeed, MathVector preferredDir) {

		int angle = preferredDir.getAzimuth();

		vec.x = Direction.getDirectionByAngle(angle).getShift().x;
		vec.y = Direction.getDirectionByAngle(angle).getShift().y;
		
		return vec.scale(factor * PacMan.getSpeed().getGain());
	}
}