package pac.man.model.level;

import java.util.Iterator;

import pac.man.ctrl.collision.CollisionHandler;
import pac.man.model.Character;
import pac.man.model.level.Level.CollisionCallback;
import pac.man.util.Dimension;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

public class Block extends Field {

	private static Paint paint = new Paint();

	static {
		paint.setColor(Color.BLUE);
		paint.setStyle(Paint.Style.FILL);
	}

	protected Block(Point position, int blockSize) {
		super(position, blockSize);
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawRect(rect, paint);
	}

	@Override
	public void handleCollision(long timeInterval, Dimension canvasDimension,
			Character character, Iterator<Field> iterator,
			CollisionHandler collisionHandler,
			CollisionCallback collisionCallback) {
		collisionHandler.handle(timeInterval, canvasDimension, rect, character);

		if (collisionCallback != null) {
			if (collisionCallback.onWall(character)) {
				iterator.remove();
			}
		}
	
	}

}
