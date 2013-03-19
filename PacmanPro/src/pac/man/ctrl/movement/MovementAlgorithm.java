package pac.man.ctrl.movement;

import pac.man.util.MathVector;

public abstract class MovementAlgorithm {

	public abstract MathVector computeSpeed(MathVector position,
			MathVector currentSpeed, MathVector prefeedDirection);
}