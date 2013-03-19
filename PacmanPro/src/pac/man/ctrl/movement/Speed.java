package pac.man.ctrl.movement;

public enum Speed {
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