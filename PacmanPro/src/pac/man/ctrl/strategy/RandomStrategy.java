package pac.man.ctrl.strategy;

import java.util.Random;

import pac.man.util.MathVector;

public class RandomStrategy extends MovementStrategy {
	private Random r = new Random(0);

	public MathVector computeDirection(MathVector position,
			MathVector currentSpeed) {
		double angle = r.nextDouble() * 2 * Math.PI;
		return new MathVector(Math.cos(angle), Math.sin(angle));
	}
}