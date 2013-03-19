package pac.man.model;

import java.util.Map;

import pac.man.ctrl.movement.NonrestrictiveMovement;
import pac.man.util.DimensionF;
import pac.man.util.MathVector;

public class Player extends Character {
	public Player(DimensionF size, MathVector position,
			Map<AnimationType, Integer> animationMapping) {

		super(size, position, animationMapping);

		// Defaults to nonrestrictive movement algorithm.
		setMovementAlgorithm(new NonrestrictiveMovement());
	}

	public void handleMove(MathVector direction) {
		if (isAlive()) {
			setSpeed(getMovementAlgorithm().computeSpeed(getPosition(),
					getSpeed(), direction));
		}
	}
}