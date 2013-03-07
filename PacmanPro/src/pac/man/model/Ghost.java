package pac.man.model;

import java.util.Map;

import android.graphics.Point;
import android.graphics.Canvas;

import pac.man.util.Animation;
import pac.man.util.MathVector;
import pac.man.model.Character.AnimationType;
import pac.man.ctrl.MovementStrategy;
import pac.man.ctrl.RandomStrategy;
import pac.man.ctrl.NonrestrictiveMovement;

public class Ghost extends Character {
    private MovementStrategy movementStrategy;
    boolean special = false;

    public Ghost(MathVector position, Map<AnimationType, Animation> animations) {
        super(new MathVector(animations.values().iterator().next().getWidth(),
                        animations.values().iterator().next().getHeight()),
              position, animations);

        // Animations:
        // - 0 - idle
        // - 1 - right
        // - 2 - up
        // - 3 - left
        // - 4 - down
        // - 5 - special
        // - 6 - death

        // Defaults to nonrestrictive, aggresive movement algorithm.
        movementAlgorithm = new NonrestrictiveMovement();
        movementStrategy = new RandomStrategy();
    }

    public void handleMove() {
        // Do dat AI, boiiii!
        MathVector dir = movementStrategy.computeDirection(getPosition(), getSpeed());
        MathVector spd = movementAlgorithm.computeSpeed(getPosition(), getSpeed(), dir);

        setSpeed(spd);
    }

    public boolean isSpecial() {
        return special;
    }

    public void setSpecial(boolean special) {
        this.special = special;
    }

    public void setMovementStrategy(MovementStrategy s) {
        movementStrategy = s;
    }

    public MovementStrategy getMovementStrategy() {
        return movementStrategy;
    }
}
