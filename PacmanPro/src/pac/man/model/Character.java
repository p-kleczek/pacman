package pac.man.model;

import java.util.Map;

import pac.man.ctrl.movement.MovementAlgorithm;
import pac.man.util.Animation;
import pac.man.util.Dimension;
import pac.man.util.MathVector;
import android.graphics.Canvas;
import android.graphics.Rect;

public abstract class Character implements Drawable {
	public static final double SPEEDUP_FACTOR = 2.3;

	private boolean alive = true;

	private final Map<AnimationType, Animation> animations;

	private final Rect boundingRect;

	private AnimationType currentAnimation = AnimationType.IDLE;
	private MovementAlgorithm movementAlgorithm;
	private final MathVector position;
	private final Dimension size;

	/**
	 * Special behaviour mode.
	 */
	protected boolean specialModeOn;

	protected final MathVector speed = new MathVector(0, 0);

	public static enum AnimationType {
		DEATH, DOWN, IDLE, LEFT, RIGHT, SPECIAL, UP
	}

	public Character(Dimension size, MathVector position,
			Map<AnimationType, Animation> animations) {
		assert size != null;
		assert position != null;
		assert animations != null;

		this.position = position;
		this.animations = animations;
		this.size = size;

		boundingRect = new Rect((int) position.x, (int) position.y,
				(int) (position.x + size.width),
				(int) (position.y + size.height));
	}

	@Override
	public void draw(final Canvas canvas) {
		Animation animation = animations.get(currentAnimation);
		animation.draw(boundingRect, canvas);
	}

	public Rect getBoundingRect() {
		return boundingRect;
	}

	public MovementAlgorithm getMovementAlgorithm() {
		return movementAlgorithm;
	}

	public MathVector getPosition() {
		return position;
	}

	public Dimension getSize() {
		return size;
	}

	public MathVector getSpeed() {
		return speed;
	}

	public boolean isAlive() {
		return alive;
	}

	public boolean isMoving() {
		return speed.length() >= 1.0;
	}

	public boolean isSpecial() {
		return specialModeOn;
	}

	public void setAlive(boolean state) {
		alive = state;

		computeActiveAnimation();
	}

	public void setMovementAlgorithm(final MovementAlgorithm a) {
		movementAlgorithm = a;
	}

	public void setPosition(final MathVector pos) {
		this.position.x = pos.x;
		this.position.y = pos.y;

		computeActiveAnimation();
	}

	public void setSpecial(final boolean s) {
		specialModeOn = s;

		computeActiveAnimation();
	}

	public void setSpeed(final MathVector speed) {
		this.speed.x = speed.x;
		this.speed.y = speed.y;

		computeActiveAnimation();
	}

	public void update(long dt, Canvas canvas) {
		final int canvasW = canvas.getWidth();
		final int canvasH = canvas.getHeight();

		final double factor = isSpecial() ? SPEEDUP_FACTOR : 1.0;

		animations.get(currentAnimation).update(dt);

		position.x += factor * speed.x * dt / 1000.0;

		if (position.x < -size.width) {
			position.x = canvasW;
		} else if (position.x > canvasW) {
			position.x = -size.width;
		}

		position.y += factor * speed.y * dt / 1000.0;

		if (position.y < -size.height) {
			position.y = canvasH;
		} else if (position.y > canvasH) {
			position.y = -size.height;
		}

		boundingRect.left = (int) position.x;
		boundingRect.right = (int) (position.x + size.width);
		boundingRect.top = (int) (position.y);
		boundingRect.bottom = (int) (position.y + size.height);
	}

	private void computeActiveAnimation() {
		if (!isAlive()) {
			setActiveAnimation(AnimationType.DEATH);
		} else if (isSpecial()) {
			setActiveAnimation(AnimationType.SPECIAL);
		} else if (!isMoving()) {
			setActiveAnimation(AnimationType.IDLE);
		} else {
			// Since the coordinate system looks like this:
			//
			// +----------------+-----------> X
			// | |
			// | |
			// | |
			// +--------------(ಠ_ಠ) <--- PacMan
			// |
			// v
			// Y
			//
			// ...we need to reverse the y coordinate when computing the angle.
			// We also add 360 degrees to eliminate negative values not to
			// produce IndexOutOfBoundsException.

			final long angle = 360L + Math.round(Math.toDegrees(Math.atan2(
					-speed.y, speed.x)));

			// Now our direction field looks like this:
			//
			// |
			// | ^
			// <- | |
			// |
			// -------+--------
			// |
			// | | ->
			// v |
			// |
			//
			// ...but we want it to look like this:
			//
			// \ ^ /
			// \ | /
			// \ /
			// \ /
			// <- X ->
			// / \
			// / \
			// / | \
			// / v \
			//
			// ...so we add extra 45 degrees to the angle rotating the
			// coordinate system.

			final int index = 1 + (int) (((angle + 45) % 360) / 90);

			AnimationType animationType;
			switch (index) {
			case 1:
				animationType = AnimationType.RIGHT;
				break;
			case 2:
				animationType = AnimationType.UP;
				break;
			case 3:
				animationType = AnimationType.LEFT;
				break;
			case 4:
				animationType = AnimationType.DOWN;
				break;
			default:
				throw new IllegalArgumentException();
			}

			setActiveAnimation(animationType);
		}
	}

	private void setActiveAnimation(final AnimationType index) {
		if (!currentAnimation.equals(index)) {
			currentAnimation = index;
			animations.get(index).reset();
		}
	}

	
}
