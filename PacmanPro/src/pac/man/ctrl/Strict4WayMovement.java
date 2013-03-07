package pac.man.ctrl;

import pac.man.util.MathVector;

public class Strict4WayMovement extends MovementAlgorithm {
    private MathVector[] speeds;
    private double factor = 1.0;

    public Strict4WayMovement(double factor) {
        this.factor = factor;
        speeds = new MathVector[4];

        // Speeds:
        // - 0 - right
        // - 1 - up
        // - 2 - left
        // - 3 - down

        speeds[0] = new MathVector(1.0, 0.0);
        speeds[1] = new MathVector(0.0, -1.0);
        speeds[2] = new MathVector(-1.0, 0.0);
        speeds[3] = new MathVector(0.0, 1.0);
    }

    public Strict4WayMovement() {
        this(1.0);
    }

    public MathVector computeSpeed(MathVector position, MathVector currentSpeed, MathVector preferredDir) {

        // Similar to Player.handleMove() logic. It's documented there.
        long angle = 360L + Math.round(Math.toDegrees(Math.atan2(-preferredDir.y, preferredDir.x)));
        long index = ((angle + 45) % 360) / 90;

        MathVector vec = new MathVector(speeds[(int) index]);
        return vec.scale(factor * MovementAlgorithm.getSpeed().getGain());
    }
}