package pac.man.model;

import java.util.Map;

import pac.man.ctrl.movement.NonrestrictiveMovement;
import pac.man.ctrl.strategy.MovementStrategy;
import pac.man.ctrl.strategy.RandomStrategy;
import pac.man.util.Animation;
import pac.man.util.DimensionF;
import pac.man.util.MathVector;

public class Ghost extends Character {
	private MovementStrategy movementStrategy;

	public Ghost(DimensionF size, MathVector position,
			Map<AnimationType, Integer> animationMapping) {
		super(size, position, animationMapping);

		// Defaults to nonrestrictive, aggresive movement algorithm.
		setMovementAlgorithm(new NonrestrictiveMovement());
		setMovementStrategy(new RandomStrategy());
	}

	public void handleMove() {
		// Do dat AI, boiiii!
		MathVector dir = movementStrategy.computeDirection(getPosition(),
				getSpeed());
		MathVector spd = getMovementAlgorithm().computeSpeed(getPosition(),
				getSpeed(), dir);

		setSpeed(spd);
	}

	public void setMovementStrategy(MovementStrategy s) {
		movementStrategy = s;
	}

	public MovementStrategy getMovementStrategy() {
		return movementStrategy;
	}
}
