package pac.man.model;

import java.util.Map;

import pac.man.ctrl.movement.NonrestrictiveMovement;
import pac.man.util.Animation;
import pac.man.util.Dimension;
import pac.man.util.MathVector;

public class Player extends Character {
	public Player(Dimension size, MathVector position,
			Map<AnimationType, Animation> animations) {

		super(size, position, animations);

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