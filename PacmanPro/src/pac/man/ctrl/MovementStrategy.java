package pac.man.ctrl;

import pac.man.util.MathVector;

public abstract class MovementStrategy {
    public abstract MathVector computeDirection(MathVector position, MathVector currentSpeed);
}