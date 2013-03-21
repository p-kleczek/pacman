package pac.man;

import pac.man.util.DimensionI;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread {
	public static final int TIME_DELTA = 20; // 20 ms <=> 50 FPS

	private SurfaceHolder surfaceHolder;
	private GamePanel gamePanel;
	private boolean running;

	public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
		super();
		this.surfaceHolder = surfaceHolder;
		this.gamePanel = gamePanel;
	}

	@Override
	public void run() {
		Canvas canvas;

		while (true) {
			// Prevent other threads' starvation.
			try {
				synchronized (this) {
					while (!running)
						wait();
				}
			} catch (InterruptedException e) {
			}

			canvas = null;
			try {
				canvas = this.surfaceHolder.lockCanvas();
				DimensionI canvasDimension = new DimensionI(canvas.getWidth(), canvas.getHeight());
				synchronized (surfaceHolder) {
					gamePanel.update(TIME_DELTA, canvasDimension);
					gamePanel.draw(canvas);
				}
			} finally {
				if (canvas != null)
					surfaceHolder.unlockCanvasAndPost(canvas);
			}
		}
	}

	synchronized public void setRunning(boolean running) {
		this.running = running;

		if (running)
			notify();
	}

	synchronized public boolean getRunning() {
		return running;
	}
}
