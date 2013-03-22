package pac.man.model;

import java.util.HashMap;
import java.util.Map;

import pac.man.ctrl.movement.Direction;
import pac.man.ctrl.movement.MovementAlgorithm;
import pac.man.util.AnimationExecutor;
import pac.man.util.Dimension;
import pac.man.util.MathVector;
import android.graphics.Canvas;
import android.graphics.Rect;

// TODO: resetowanie animacji "death" (np. dla gracza: w chwili kolizji z duszkiem (badz przy ponownych narodzinach))

public abstract class Character implements Drawable {
	public static final double SPEEDUP_FACTOR = 2.3;

	private boolean alive = true;

	private final Map<AnimationType, AnimationExecutor> animations;

	private final Rect boundingRect;

	private AnimationType currentAnimation = AnimationType.IDLE;
	private MovementAlgorithm movementAlgorithm;
	private final MathVector position;
	private final Dimension size;

	/**
	 * Special behaviour mode.
	 */
	protected boolean specialModeOn;

	protected final MathVector speed = new MathVector();

	public static enum AnimationType {
		DEATH, DOWN, IDLE, LEFT, RIGHT, SPECIAL, UP
	}

	public Character(Dimension size, MathVector position,
			Map<AnimationType, Integer> animationMapping) {
		this.position = position;
		this.size = size;

		this.animations = new HashMap<AnimationType, AnimationExecutor>();
		for (AnimationType t : animationMapping.keySet()) {
			animations.put(t, new AnimationExecutor(animationMapping.get(t)));
		}

		boundingRect = new Rect((int) position.x, (int) position.y,
				(int) (position.x + size.width),
				(int) (position.y + size.height));
	}

	private void computeCurrentAnimationType() {
		if (!isAlive()) {
			currentAnimation = AnimationType.DEATH;
		} else if (isSpecial()) {
			currentAnimation = AnimationType.SPECIAL;
		} else if (!isMoving()) {
			currentAnimation = AnimationType.IDLE;
		} else {
			int angle = speed.getAzimuth();

			// FIXME : dlaczego takie pomieszanie UP/DOWN?
			switch (Direction.getDirectionByAngle(angle)) {
			case N:
				currentAnimation = AnimationType.DOWN;
				break;
			case S:
				currentAnimation = AnimationType.UP;
				break;
			case W:
				currentAnimation = AnimationType.LEFT;
				break;
			case E:
				currentAnimation = AnimationType.RIGHT;
				break;
			default:
				throw new IllegalArgumentException();
			}
		}
	}

	@Override
	public void draw(final Canvas canvas) {
		computeCurrentAnimationType();
		AnimationExecutor animation = animations.get(currentAnimation);
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
	}

	public void setMovementAlgorithm(final MovementAlgorithm a) {
		movementAlgorithm = a;
	}

	public void setPosition(final MathVector pos) {
		position.set(pos);
	}

	public void setSpecial(boolean s) {
		specialModeOn = s;
	}

	public void setSpeed(final MathVector speed) {
		this.speed.set(speed);
	}

	private void updatePosition(long timeInterval, Dimension canvasSize) {
		final double factor = (isSpecial() ? SPEEDUP_FACTOR : 1.0) / 1000.0;

		position.x += factor * speed.x * timeInterval;
		position.y += factor * speed.y * timeInterval;

		// Restrict move to board.
		if (position.x < -size.width) {
			position.x = canvasSize.width;
		} else if (position.x > canvasSize.width) {
			position.x = -size.width;
		}

		if (position.y < -size.height) {
			position.y = canvasSize.height;
		} else if (position.y > canvasSize.height) {
			position.y = -size.height;
		}
	}

	private void updateBoundingRect(Dimension canvasSize) {
		boundingRect.left = (int) position.x;
		boundingRect.right = (int) (position.x + size.width);
		boundingRect.top = (int) (position.y);
		boundingRect.bottom = (int) (position.y + size.height);
	}

	private void updateAnimation(long dt) {
		animations.get(currentAnimation).update(dt);
	}

	public void update(long timeInterval, Dimension canvasSize) {
		updatePosition(timeInterval, canvasSize);
		updateBoundingRect(canvasSize);
		updateAnimation(timeInterval);
	}

}
