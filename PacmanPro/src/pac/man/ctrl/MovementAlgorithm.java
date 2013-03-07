package pac.man.ctrl;

import pac.man.util.MathVector;

public abstract class MovementAlgorithm {
    public static enum Speed {
        SLOW(40, "Slow"), NORMAL(60, "Normal"), FAST(80, "Fast");

        private final int gain;
        private final String label;
        private Speed(int gain, String label) {
            this.gain = gain;
            this.label = label;
        }
        public int getGain() {
            return gain;
        }
        public String getLabel() {
            return label;
        }
    }

    private static Speed speed = Speed.NORMAL;

    public static Speed getSpeed() {
        return speed;
    }

    public static void setSpeed(Speed speed) {
        MovementAlgorithm.speed = speed;
    }

    public abstract MathVector computeSpeed(MathVector position, MathVector currentSpeed, MathVector prefeedDirection);
}