package pac.man.ctrl.collision;

import pac.man.model.Character;
import pac.man.util.DimensionI;
import pac.man.util.MathVector;
import android.graphics.Rect;

public class BouncyCollisions implements CollisionHandler {
	public void handle(long dt, DimensionI canvasDimension, Rect rect, Character c) {
		MathVector speed = c.getSpeed();
		speed.scale(-1.0);

		c.setSpeed(speed);
		Rect b;

		do {
			c.update(dt, canvasDimension); // reverse for a while
			b = c.getBoundingRect();
		} while (Rect.intersects(b, rect));

		speed.scale(-1.0);

		// TODO Compute the angle dependantly on movement direction.
		double angle = 180;

		speed.rotate(angle);
		c.setSpeed(speed);
	}
}