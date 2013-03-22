package pac.man.util;

import android.graphics.Bitmap;

public class Animation {
	private final Bitmap bitmap;
	private final int numFrames;
	private final int framePeriod;
	private final Dimension frameDimension;

	public Animation(final Bitmap bitmap, final int numFrames, final int period) {
		this.bitmap = bitmap;
		this.numFrames = numFrames;
		this.framePeriod = period / numFrames;

		frameDimension = new Dimension(bitmap.getWidth() / numFrames, bitmap.getHeight());
	}

	public Dimension getFrameDimension() {
		return frameDimension;
	}
	
	public int getNumFrames() {
		return numFrames;
	}

	public int getFramePeriod() {
		return framePeriod;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	
}