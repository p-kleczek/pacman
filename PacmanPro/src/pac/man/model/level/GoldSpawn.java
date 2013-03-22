package pac.man.model.level;

import java.util.Iterator;

import pac.man.R;
import pac.man.ctrl.collision.CollisionHandler;
import pac.man.model.Character;
import pac.man.model.level.Level.CollisionCallback;
import pac.man.util.AnimationExecutor;
import pac.man.util.Dimension;
import android.graphics.Canvas;
import android.graphics.Point;

public class GoldSpawn extends Field {

	private static AnimationExecutor animationExecutor = new AnimationExecutor(
			R.drawable.gold);

	protected GoldSpawn(Point position, int blockSize,
			CollisionHandler collisionHandlerArg,
			CollisionCallback collisionCallbackArg) {
		super(position, blockSize, collisionHandlerArg, collisionCallbackArg);
		scaleBoundaryBox();
	}

	private void scaleBoundaryBox() {
		final double BOUNDARY_BOX_SCALE_FACTOR = 0.3;
		rect.left += animationExecutor.getFrameDimension().width
				* BOUNDARY_BOX_SCALE_FACTOR;
		rect.right -= animationExecutor.getFrameDimension().width
				* BOUNDARY_BOX_SCALE_FACTOR;
		rect.top += animationExecutor.getFrameDimension().height
				* BOUNDARY_BOX_SCALE_FACTOR;
		rect.bottom -= animationExecutor.getFrameDimension().height
				* BOUNDARY_BOX_SCALE_FACTOR;
	}

	@Override
	public void draw(Canvas canvas) {
		animationExecutor.draw(rect, canvas);
	}

	@Override
	public void handleCollision(long timeInterval, Dimension canvasDimension,
			Character character, Iterator<Field> iterator) {

		if (collisionCallback != null) {
			if (collisionCallback.onGold(character)) {
				iterator.remove();
			}
		}
	}

	public static void updateAnimationExecutor(long timeInterval) {
		animationExecutor.update(timeInterval);
	}

}
