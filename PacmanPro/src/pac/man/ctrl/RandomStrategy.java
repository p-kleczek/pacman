package pac.man.ctrl;

import java.util.Random;
import pac.man.util.MathVector;

public class RandomStrategy extends MovementStrategy {
    private Random r = new Random();

    public MathVector computeDirection(MathVector position, MathVector currentSpeed) {
        double angle = r.nextDouble() * 2 * Math.PI;
        return new MathVector(Math.cos(angle), Math.sin(angle));
    }
}