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
	protected static CollisionHandler collisionHandler;
	protected static CollisionCallback collisionCallback;

	protected Field(Point position, int blockSize, CollisionHandler collisionHandlerArg,
			CollisionCallback collisionCallbackArg) {
		assert collisionCallbackArg != null;
		assert collisionHandlerArg != null;

		rect.set(position.x * blockSize, position.y * blockSize, (position.x + 1) * blockSize,
				(position.y + 1) * blockSize);		
		
		collisionHandler = collisionHandlerArg;
		collisionCallback = collisionCallbackArg;
	}

	public Rect getRect() {
		return rect;
	}

	public abstract void handleCollision(long timeInterval,
			Dimension canvasDimension, Character character,
			Iterator<Field> iterator);
}
