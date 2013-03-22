package pac.man.model.level;

import java.util.Iterator;

import pac.man.ctrl.collision.CollisionHandler;
import pac.man.model.Character;
import pac.man.model.Drawable;
import pac.man.model.level.Level.CollisionCallback;
import pac.man.util.Dimension;
import android.graphics.Point;
import android.graphics.Rect;

public abstract class Field implements Drawable {

	protected final Rect rect = new Rect();

	protected Field(Point position, int blockSize) {

		rect.set(position.x * blockSize, position.y * blockSize, (position.x + 1) * blockSize,
				(position.y + 1) * blockSize);		
	}

	public Rect getRect() {
		return rect;
	}

	public abstract void handleCollision(long timeInterval,
			Dimension canvasDimension, Character character,
			Iterator<Field> iterator, CollisionHandler collisionHandler,
			CollisionCallback collisionCallback);
}
