package pac.man.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Animation {
	private final Bitmap bitmap;
	private int currentFrame;
	private final int numFrames;
	private long frameTicker;
	private final int framePeriod;
	private final int spriteWidth;
	private final int spriteHeight;
	private final Rect sourceRect;

	public Animation(final Bitmap bitmap, final int numFrames, final int period) {
		this.bitmap = bitmap;
		this.spriteWidth = bitmap.getWidth() / numFrames;
		this.spriteHeight = bitmap.getHeight();
		this.numFrames = numFrames;
		this.framePeriod = period / numFrames;

		sourceRect = new Rect(0, 0, spriteWidth, spriteHeight);
	}

	public void draw(final Rect destRect, final Canvas canvas) {
		// Rect destRect = new Rect(position.x, position.y,
		// position.x + spriteWidth, position.y + spriteHeight);
		canvas.drawBitmap(bitmap, sourceRect, destRect, null);
	}

	public void update(final long dt) {
		frameTicker += dt;

		if (frameTicker >= framePeriod) {
			frameTicker -= framePeriod;
			currentFrame = (currentFrame + 1) % numFrames;
		}

		sourceRect.left = currentFrame * spriteWidth;
		sourceRect.right = sourceRect.left + spriteWidth;
	}

	public void reset() {
		currentFrame = 0;
		frameTicker = 0;
	}

	public int getWidth() {
		return spriteWidth;
	}

	public int getHeight() {
		return spriteHeight;
	}
}