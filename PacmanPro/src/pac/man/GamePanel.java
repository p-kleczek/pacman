package pac.man;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

import pac.man.model.Character;
import pac.man.model.Character.AnimationType;
import pac.man.model.GameState;
import pac.man.model.Level;
import pac.man.model.Player;
import pac.man.util.Animation;
import pac.man.util.Dimension;
import pac.man.util.MathVector;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
	// private Context context;
	private final MainThread thread;
	private final ResourceManager resMgr;
	private boolean initalized;

	private int levelCounter;
	private Level[] levels;

	private GameState gameState;
	private Player player;

	public GamePanel(final Context context) {
		super(context);
		getHolder().addCallback(this);
		setFocusable(true);

		// this.context = context;

		resMgr = new ResourceManager(this, context);

		thread = new MainThread(getHolder(), this);
		thread.start();
	}

	private void init() {
		// Levels
		levels = new Level[6];
		levels[0] = resMgr.getLevel(R.raw.level1);
		levels[1] = resMgr.getLevel(R.raw.level2);
		levels[2] = resMgr.getLevel(R.raw.level3);
		levels[3] = resMgr.getLevel(R.raw.level4);
		levels[4] = resMgr.getLevel(R.raw.test_level);
		levels[5] = resMgr.getLevel(R.raw.test_level2);

		// Sounds
		resMgr.loadSound(R.raw.coin);
		resMgr.loadSound(R.raw.powerup);
		resMgr.loadSound(R.raw.death);

		resMgr.loadSound(R.raw.pacman_death);
		resMgr.loadSound(R.raw.pacman_chomp);
		resMgr.loadSound(R.raw.pacman_eatghost);
		resMgr.loadSound(R.raw.pacman_eatfruit);
		resMgr.loadSound(R.raw.pacman_intermission);

		resMgr.loadSound(R.raw.pacman_death);
		resMgr.loadSound(R.raw.pacman_chomp);
		resMgr.loadSound(R.raw.pacman_eatghost);
		resMgr.loadSound(R.raw.pacman_eatfruit);
		resMgr.loadSound(R.raw.pacman_intermission);

		// Animations
		final Map<Character.AnimationType, Animation> animations = new EnumMap<Character.AnimationType, Animation>(
				Character.AnimationType.class);

		// XXX: Shit, this is ugly...
		final ArrayList<Map<Character.AnimationType, Animation>> ghosts = new ArrayList<Map<Character.AnimationType, Animation>>();
		ghosts.add(new EnumMap<Character.AnimationType, Animation>(
				Character.AnimationType.class));
		ghosts.add(new EnumMap<Character.AnimationType, Animation>(
				Character.AnimationType.class));
		ghosts.add(new EnumMap<Character.AnimationType, Animation>(
				Character.AnimationType.class));
		ghosts.add(new EnumMap<Character.AnimationType, Animation>(
				Character.AnimationType.class));

		// Player animootions.
		animations.put(AnimationType.IDLE,
				resMgr.getAnimation(R.drawable.idle, 1, 10000));
		animations.put(AnimationType.RIGHT,
				resMgr.getAnimation(R.drawable.right, 4, 500));
		animations.put(AnimationType.UP,
				resMgr.getAnimation(R.drawable.up, 4, 500));
		animations.put(AnimationType.LEFT,
				resMgr.getAnimation(R.drawable.left, 4, 500));
		animations.put(AnimationType.DOWN,
				resMgr.getAnimation(R.drawable.down, 4, 500));
		animations.put(AnimationType.SPECIAL,
				resMgr.getAnimation(R.drawable.boing, 2, 500));
		animations.put(AnimationType.DEATH,
				resMgr.getAnimation(R.drawable.death, 13, 750));

		// Ghost animutions
		ghosts.get(0).put(AnimationType.IDLE,
				resMgr.getAnimation(R.drawable.red_down, 2, 500));
		ghosts.get(0).put(AnimationType.RIGHT,
				resMgr.getAnimation(R.drawable.red_right, 2, 500));
		ghosts.get(0).put(AnimationType.UP,
				resMgr.getAnimation(R.drawable.red_up, 2, 500));
		ghosts.get(0).put(AnimationType.DOWN,
				resMgr.getAnimation(R.drawable.red_down, 2, 500));
		ghosts.get(0).put(AnimationType.LEFT,
				resMgr.getAnimation(R.drawable.red_left, 2, 500));
		ghosts.get(0).put(AnimationType.DEATH,
				resMgr.getAnimation(R.drawable.ill_white, 2, 500));
		ghosts.get(0).put(AnimationType.SPECIAL,
				resMgr.getAnimation(R.drawable.ill_blue, 2, 500));

		ghosts.get(1).put(AnimationType.IDLE,
				resMgr.getAnimation(R.drawable.green_down, 2, 500));
		ghosts.get(1).put(AnimationType.RIGHT,
				resMgr.getAnimation(R.drawable.green_right, 2, 500));
		ghosts.get(1).put(AnimationType.UP,
				resMgr.getAnimation(R.drawable.green_up, 2, 500));
		ghosts.get(1).put(AnimationType.DOWN,
				resMgr.getAnimation(R.drawable.green_down, 2, 500));
		ghosts.get(1).put(AnimationType.LEFT,
				resMgr.getAnimation(R.drawable.green_left, 2, 500));
		ghosts.get(1).put(AnimationType.DEATH,
				resMgr.getAnimation(R.drawable.ill_white, 2, 500));
		ghosts.get(1).put(AnimationType.SPECIAL,
				resMgr.getAnimation(R.drawable.ill_blue, 2, 500));

		ghosts.get(2).put(AnimationType.IDLE,
				resMgr.getAnimation(R.drawable.blue_down, 2, 500));
		ghosts.get(2).put(AnimationType.RIGHT,
				resMgr.getAnimation(R.drawable.blue_right, 2, 500));
		ghosts.get(2).put(AnimationType.UP,
				resMgr.getAnimation(R.drawable.blue_up, 2, 500));
		ghosts.get(2).put(AnimationType.DOWN,
				resMgr.getAnimation(R.drawable.blue_down, 2, 500));
		ghosts.get(2).put(AnimationType.LEFT,
				resMgr.getAnimation(R.drawable.blue_left, 2, 500));
		ghosts.get(2).put(AnimationType.DEATH,
				resMgr.getAnimation(R.drawable.ill_white, 2, 500));
		ghosts.get(2).put(AnimationType.SPECIAL,
				resMgr.getAnimation(R.drawable.ill_blue, 2, 500));

		ghosts.get(3).put(AnimationType.IDLE,
				resMgr.getAnimation(R.drawable.orange_down, 2, 500));
		ghosts.get(3).put(AnimationType.RIGHT,
				resMgr.getAnimation(R.drawable.orange_right, 2, 500));
		ghosts.get(3).put(AnimationType.UP,
				resMgr.getAnimation(R.drawable.orange_up, 2, 500));
		ghosts.get(3).put(AnimationType.DOWN,
				resMgr.getAnimation(R.drawable.orange_down, 2, 500));
		ghosts.get(3).put(AnimationType.LEFT,
				resMgr.getAnimation(R.drawable.orange_left, 2, 500));
		ghosts.get(3).put(AnimationType.DEATH,
				resMgr.getAnimation(R.drawable.ill_white, 2, 500));
		ghosts.get(3).put(AnimationType.SPECIAL,
				resMgr.getAnimation(R.drawable.ill_blue, 2, 500));

		Animation samplePlayerAnimationFrame = animations.values().iterator()
				.next();
		Dimension playerSize = new Dimension(
				samplePlayerAnimationFrame.getWidth(),
				samplePlayerAnimationFrame.getHeight());

		player = new Player(playerSize, new MathVector(-200, 0), animations);
		gameState = new GameState(player, levels[levelCounter], ghosts, resMgr);

		gameState.setNumOpponents(PacMan.pNOps);

		initalized = true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	public GameState getGameState() {
		return gameState;
	}

	public Player getPlayer() {
		return player;
	}

	public void surfaceCreated(SurfaceHolder holder) {
		if (!initalized) {
			init();
			thread.setRunning(true);
		}

		redraw();
	}

	private void redraw() {
		// Just repaint the board and wait for the user to resume the game.
		Canvas canvas = null;
		try {
			canvas = getHolder().lockCanvas();
			synchronized (getHolder()) {
				draw(canvas);
			}
		} finally {
			if (canvas != null)
				getHolder().unlockCanvasAndPost(canvas);
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		thread.setRunning(false);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		final boolean isMouseAction = event.getAction() == MotionEvent.ACTION_DOWN
				|| event.getAction() == MotionEvent.ACTION_MOVE;

		if (!thread.getRunning() && isMouseAction) {
			final MathVector ppos = player.getPosition();
			final Dimension psize = player.getSize();
			final MathVector touch = new MathVector(event.getX(), event.getY());

			final MathVector direction = new MathVector(touch.x - ppos.x
					- psize.width / 2, touch.y - ppos.y - psize.height / 2);

			direction.normalize();
			player.handleMove(direction);
		}

		return true;
	}

	public MainThread getThread() {
		return thread;
	}

	public void draw(Canvas canvas) {
		canvas.drawColor(Color.BLACK);

		gameState.draw(canvas);

		final int score = gameState.getScore();
		final int lives = gameState.getLives();

		final Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setTextSize(22);

		canvas.drawText(String.format("%d", score), 10, getHeight() - 20, paint);

		paint.setColor(Color.MAGENTA);
		canvas.drawText(String.format("%d", lives), getWidth() - 20,
				getHeight() - 20, paint);
	}

	public void update(long dt, Canvas canvas) {
		gameState.update(dt, canvas);

		if (gameState.playerWon()) {
			levelCounter = (levelCounter + 1) % levels.length;
			PacMan.showMessage(String.format("Level %d", levelCounter + 1));

			gameState.setLevel(levels[levelCounter]);
		}
	}

	public void restartLevel() {
		thread.setRunning(false);

		PacMan.showMessage("Level restarted.");
		gameState.restartLevel();

		thread.setRunning(true);
		redraw();
		thread.setRunning(false);
	}

	public ResourceManager getResourceManager() {
		return resMgr;
	}
}
