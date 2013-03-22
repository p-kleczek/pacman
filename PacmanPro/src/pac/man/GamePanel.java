package pac.man;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

import pac.man.model.Character;
import pac.man.model.Character.AnimationType;
import pac.man.model.level.Level;
import pac.man.model.GameState;
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

		thread = new MainThread(getHolder(), this);
		thread.start();
	}

	private void init() {
		ResourceManager.loadAnimation(this, R.drawable.cherry, 2, 4000);
		ResourceManager.loadAnimation(this, R.drawable.gold, 2, 4000);

		// Sounds
		ResourceManager.loadSound(getContext(), R.raw.coin);
		ResourceManager.loadSound(getContext(), R.raw.powerup);
		ResourceManager.loadSound(getContext(), R.raw.death);

		ResourceManager.loadSound(getContext(), R.raw.pacman_death);
		ResourceManager.loadSound(getContext(), R.raw.pacman_chomp);
		ResourceManager.loadSound(getContext(), R.raw.pacman_eatghost);
		ResourceManager.loadSound(getContext(), R.raw.pacman_eatfruit);
		ResourceManager.loadSound(getContext(), R.raw.pacman_intermission);

		ResourceManager.loadSound(getContext(), R.raw.pacman_death);
		ResourceManager.loadSound(getContext(), R.raw.pacman_chomp);
		ResourceManager.loadSound(getContext(), R.raw.pacman_eatghost);
		ResourceManager.loadSound(getContext(), R.raw.pacman_eatfruit);
		ResourceManager.loadSound(getContext(), R.raw.pacman_intermission);

		// Animations

		final Map<Character.AnimationType, Integer> animations = new EnumMap<Character.AnimationType, Integer>(
				Character.AnimationType.class);

		// XXX: Shit, this is ugly...
		final ArrayList<Map<AnimationType, Integer>> ghosts = new ArrayList<Map<Character.AnimationType, Integer>>();
		ghosts.add(new EnumMap<Character.AnimationType, Integer>(
				Character.AnimationType.class));
		ghosts.add(new EnumMap<Character.AnimationType, Integer>(
				Character.AnimationType.class));
		ghosts.add(new EnumMap<Character.AnimationType, Integer>(
				Character.AnimationType.class));
		ghosts.add(new EnumMap<Character.AnimationType, Integer>(
				Character.AnimationType.class));

		ResourceManager.loadAnimation(this, R.drawable.idle, 1, 10000);
		ResourceManager.loadAnimation(this, R.drawable.right, 4, 500);
		ResourceManager.loadAnimation(this, R.drawable.up, 4, 500);
		ResourceManager.loadAnimation(this, R.drawable.left, 4, 500);
		ResourceManager.loadAnimation(this, R.drawable.down, 4, 500);
		ResourceManager.loadAnimation(this, R.drawable.boing, 2, 500);
		ResourceManager.loadAnimation(this, R.drawable.death, 13, 750);

		// Player animations.
		animations.put(AnimationType.IDLE, R.drawable.idle);
		animations.put(AnimationType.RIGHT, R.drawable.right);
		animations.put(AnimationType.UP, R.drawable.up);
		animations.put(AnimationType.LEFT, R.drawable.left);
		animations.put(AnimationType.DOWN, R.drawable.down);
		animations.put(AnimationType.SPECIAL, R.drawable.boing);
		animations.put(AnimationType.DEATH, R.drawable.death);

		int[] ids = new int[] { R.drawable.red_right, R.drawable.red_up,
				R.drawable.red_left, R.drawable.red_down, R.drawable.ill_white,
				R.drawable.ill_blue, R.drawable.blue_right, R.drawable.blue_up,
				R.drawable.blue_left, R.drawable.blue_down,
				R.drawable.green_right, R.drawable.green_up,
				R.drawable.green_left, R.drawable.green_down,
				R.drawable.orange_right, R.drawable.orange_up,
				R.drawable.orange_left, R.drawable.orange_down };

		for (int id : ids) {
			ResourceManager.loadAnimation(this, id, 2, 500);
		}

		// Ghost animutions
		ghosts.get(0).put(AnimationType.IDLE, R.drawable.red_down);
		ghosts.get(0).put(AnimationType.RIGHT, R.drawable.red_right);
		ghosts.get(0).put(AnimationType.LEFT, R.drawable.red_left);
		ghosts.get(0).put(AnimationType.UP, R.drawable.red_up);
		ghosts.get(0).put(AnimationType.DOWN, R.drawable.red_down);
		ghosts.get(0).put(AnimationType.DEATH, R.drawable.ill_white);
		ghosts.get(0).put(AnimationType.SPECIAL, R.drawable.ill_blue);

		ghosts.get(1).put(AnimationType.IDLE, R.drawable.blue_down);
		ghosts.get(1).put(AnimationType.RIGHT, R.drawable.blue_right);
		ghosts.get(1).put(AnimationType.LEFT, R.drawable.blue_left);
		ghosts.get(1).put(AnimationType.UP, R.drawable.blue_up);
		ghosts.get(1).put(AnimationType.DOWN, R.drawable.blue_down);
		ghosts.get(1).put(AnimationType.DEATH, R.drawable.ill_white);
		ghosts.get(1).put(AnimationType.SPECIAL, R.drawable.ill_blue);

		ghosts.get(2).put(AnimationType.IDLE, R.drawable.green_down);
		ghosts.get(2).put(AnimationType.RIGHT, R.drawable.green_right);
		ghosts.get(2).put(AnimationType.LEFT, R.drawable.green_left);
		ghosts.get(2).put(AnimationType.UP, R.drawable.green_up);
		ghosts.get(2).put(AnimationType.DOWN, R.drawable.green_down);
		ghosts.get(2).put(AnimationType.DEATH, R.drawable.ill_white);
		ghosts.get(2).put(AnimationType.SPECIAL, R.drawable.ill_blue);

		ghosts.get(3).put(AnimationType.IDLE, R.drawable.orange_down);
		ghosts.get(3).put(AnimationType.RIGHT, R.drawable.orange_right);
		ghosts.get(3).put(AnimationType.LEFT, R.drawable.orange_left);
		ghosts.get(3).put(AnimationType.UP, R.drawable.orange_up);
		ghosts.get(3).put(AnimationType.DOWN, R.drawable.orange_down);
		ghosts.get(3).put(AnimationType.DEATH, R.drawable.ill_white);
		ghosts.get(3).put(AnimationType.SPECIAL, R.drawable.ill_blue);

		Integer sampleId = animations.values().iterator().next();
		Animation samplePlayerAnimationFrame = ResourceManager
				.getAnimation(sampleId);
		Dimension playerSize = new Dimension(
				samplePlayerAnimationFrame.getFrameDimension().width,
				samplePlayerAnimationFrame.getFrameDimension().height);

		player = new Player(playerSize, new MathVector(-200, 0), animations);

		// Levels
		levels = new Level[6];
		levels[0] = ResourceManager.getLevel(this, R.raw.level1);
		levels[1] = ResourceManager.getLevel(this, R.raw.level2);
		levels[2] = ResourceManager.getLevel(this, R.raw.level3);
		levels[3] = ResourceManager.getLevel(this, R.raw.level4);
		levels[4] = ResourceManager.getLevel(this, R.raw.test_level);
		levels[5] = ResourceManager.getLevel(this, R.raw.test_level2);

		gameState = new GameState(player, levels[levelCounter], ghosts,
				getContext());

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

		if (thread.isRunning() && isMouseAction) {
			final MathVector ppos = player.getPosition();
			final Dimension psize = player.getSize();
			final MathVector touch = new MathVector(event.getX(), event.getY());

			final MathVector direction = new MathVector(touch.x - ppos.x
					- psize.width / 2, -(touch.y - ppos.y - psize.height / 2));

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

	public void update(long dt, Dimension canvasDimension) {
		gameState.update(dt, canvasDimension);

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
}
