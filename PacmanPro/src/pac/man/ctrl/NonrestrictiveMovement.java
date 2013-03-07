package pac.man.ctrl;

import pac.man.util.MathVector;

public class NonrestrictiveMovement extends MovementAlgorithm {
    private double factor = 1.0;

    public NonrestrictiveMovement(double factor) {
        this.factor = factor;
    }

    public NonrestrictiveMovement() {
        this(1.0);
    }

    public MathVector computeSpeed(MathVector position, MathVector currentSpeed, MathVector preferredDir) {
        // PreferredDirection ought to be normalized here.
        preferredDir.scale(factor * MovementAlgorithm.getSpeed().getGain());

        // Just return the direction.
        return preferredDir;
    }
}