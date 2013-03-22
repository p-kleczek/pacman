package pac.man.ctrl.collision;

import pac.man.model.Character;
import pac.man.util.Dimension;
import pac.man.util.MathVector;
import android.graphics.Rect;

public class StickyCollisions implements CollisionHandler {
	public void handle(long dt,Dimension canvasDimension, Rect rect, Character c) {
		MathVector speed = c.getSpeed();
		speed.scale(-1.0);

		c.setSpeed(speed);
		Rect b;
		do {
			c.update(dt, canvasDimension); // reverse for a while
			b = c.getBoundingRect();
		} while (Rect.intersects(b, rect));

		c.setSpeed(new MathVector(0, 0));
	}
}