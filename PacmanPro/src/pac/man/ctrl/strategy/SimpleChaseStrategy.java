package pac.man.ctrl.strategy;

import pac.man.model.Player;
import pac.man.util.MathVector;

public class SimpleChaseStrategy extends RandomStrategy {
	private Player player;
	private double range;
	private double factor;

	public SimpleChaseStrategy(Player player, double range, double factor) {
		super();

		assert player != null;
		assert range > 0.0;

		this.player = player;
		this.range = range;
		this.factor = factor;
	}

	public MathVector computeDirection(MathVector position,
			MathVector currentSpeed) {
		MathVector ppos = player.getPosition();

		if (position.distanceTo(ppos) <= range) {
			return new MathVector(ppos.x - position.x, ppos.y - position.y)
					.normalize().scale(factor);
		} else {
			return super.computeDirection(position, currentSpeed);
		}
	}
}