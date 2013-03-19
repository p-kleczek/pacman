package pac.man.ctrl.strategy;

import pac.man.util.MathVector;

public abstract class MovementStrategy {
	public abstract MathVector computeDirection(MathVector position,
			MathVector currentSpeed);
}