package pac.man.ctrl;

import pac.man.util.MathVector;

public class InertialMovement extends MovementAlgorithm {
    private MathVector speed;
    private double inertia;

    public InertialMovement(MathVector speed, double inertia) {
        this.speed = new MathVector(speed.x, speed.y);
        this.inertia = inertia;
    }

    public MathVector computeSpeed(MathVector position, MathVector currentSpeed, MathVector preferredDir) {
        speed.add(preferredDir.scale(inertia));
        speed.normalize();
        return speed.scale(MovementAlgorithm.getSpeed().getGain());
    }
}