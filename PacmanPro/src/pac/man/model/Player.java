package pac.man.model;

import java.util.Map;

import pac.man.ctrl.NonrestrictiveMovement;
import pac.man.util.Animation;
import pac.man.util.MathVector;

public class Player extends Character {
	public Player(MathVector position, Map<AnimationType, Animation> animations) {
		super(new MathVector(animations.values().iterator().next().getWidth(),
				animations.values().iterator().next().getHeight()), position,
				animations);

		assert animations.keySet().size() >= 6;

		// Animations:
		// - 0 - idle
		// - 1 - right
		// - 2 - up
		// - 3 - left
		// - 4 - down
		// - 5 - death

		// Defaults to nonrestrictive movement algorithm.
		movementAlgorithm = new NonrestrictiveMovement();
	}

	public void handleMove(final MathVector direction) {
		if (isAlive()) {
			setSpeed(movementAlgorithm.computeSpeed(getPosition(), getSpeed(),
					direction));
		}
	}
}