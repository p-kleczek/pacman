package pac.man.ctrl.collision;

import pac.man.model.Character;
import pac.man.util.Dimension;
import pac.man.util.MathVector;
import android.graphics.Rect;

public class StickyCollisions implements CollisionHandler {
	public void handle(long timeInterval, Dimension canvasDimension, Rect objectBoundary,
			Character character) {
		MathVector speed = character.getSpeed();
		speed.scale(-1.0);

		character.setSpeed(speed);
		do {
			character.update(timeInterval, canvasDimension); // reverse for a while
		} while (Rect.intersects(character.getBoundingRect(), objectBoundary));

		character.setSpeed(new MathVector());
	}
}