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

public class PowerSpawn extends Field {

	 // Fake independant animations.
	private static final long AWFUL_HAX = 23;

	// XXX : nie da sie wydzielic wspolnej klasy AnimatedField? wrzucic do niej
	// uaktualnienie wymiarow o rozmiar animacji i uaktualnienie czasu

	private static AnimationExecutor animationExecutor = new AnimationExecutor(
			R.drawable.cherry);

	protected PowerSpawn(Point position, int blockSize) {
		super(position, blockSize);
	}

	@Override
	public void draw(Canvas canvas) {
		animationExecutor.draw(rect, canvas);
		animationExecutor.update(AWFUL_HAX);
	}

	@Override
	public void handleCollision(long timeInterval, Dimension canvasDimension,
			Character character, Iterator<Field> iterator,
			CollisionHandler collisionHandler,
			CollisionCallback collisionCallback) {
		if (collisionCallback != null) {
			if (collisionCallback.onPowerup(character)) {
				iterator.remove();
			}
		}
	}

	public static void updateAnimationExecutor(long timeInterval) {
		animationExecutor.update(timeInterval);
	}

}
