package pac.man.util;

import pac.man.ResourceManager;
import android.graphics.Canvas;
import android.graphics.Rect;

public class AnimationExecutor {
	private final Animation animation;
	private int currentFrame;
	private long frameTicker;
	private final Rect sourceRect;

	public AnimationExecutor(int animationId) {
		animation = ResourceManager.getAnimation(Integer.valueOf(animationId));
		sourceRect = new Rect(0, 0, animation.getFrameDimension().width,
				animation.getFrameDimension().height);
	}

	public void update(final long dt) {
		frameTicker += dt;

		if (frameTicker >= animation.getFramePeriod()) {
			frameTicker -= animation.getFramePeriod();
			currentFrame = (currentFrame + 1) % animation.getNumFrames();
		}

		sourceRect.left = currentFrame * animation.getFrameDimension().width;
		sourceRect.right = sourceRect.left
				+ animation.getFrameDimension().width;
	}

	public void reset() {
		currentFrame = 0;
		frameTicker = 0;
	}

	public void draw(final Rect destRect, final Canvas canvas) {
		// Rect destRect = new Rect(position.x, position.y,
		// position.x + spriteWidth, position.y + spriteHeight);
		canvas.drawBitmap(animation.getBitmap(), sourceRect, destRect, null);
	}

	public DimensionI getFrameDimension() {
		return animation.getFrameDimension();
	}
}
