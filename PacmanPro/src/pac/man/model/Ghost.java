package pac.man.model;

import java.util.Map;

import pac.man.ctrl.movement.NonrestrictiveMovement;
import pac.man.ctrl.strategy.MovementStrategy;
import pac.man.ctrl.strategy.RandomStrategy;
import pac.man.util.Dimension;
import pac.man.util.MathVector;

public class Ghost extends Character {
	private MovementStrategy movementStrategy;

	public Ghost(Dimension size, MathVector position,
			Map<AnimationType, Integer> animationMapping) {
		super(size, position, animationMapping);

		// Defaults to nonrestrictive, aggresive movement algorithm.
		setMovementAlgorithm(new NonrestrictiveMovement());
		setMovementStrategy(new RandomStrategy());
	}

	public void handleMove() {
		// Do dat AI, boiiii!
		MathVector direction = movementStrategy.computeDirection(getPosition(),
				getSpeed());
		MathVector speed = getMovementAlgorithm().computeSpeed(getPosition(),
				getSpeed(), direction);

		setSpeed(speed);
	}

	public void setMovementStrategy(MovementStrategy s) {
		movementStrategy = s;
	}

	public MovementStrategy getMovementStrategy() {
		return movementStrategy;
	}
}
