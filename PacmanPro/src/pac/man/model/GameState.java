package pac.man.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pac.man.PacMan;
import pac.man.R;
import pac.man.ResourceManager;
import pac.man.ctrl.collision.BouncyCollisions;
import pac.man.ctrl.collision.StickyCollisions;
import pac.man.ctrl.movement.InertialMovement;
import pac.man.ctrl.movement.NonrestrictiveMovement;
import pac.man.ctrl.movement.Strict4WayMovement;
import pac.man.ctrl.strategy.RandomStrategy;
import pac.man.ctrl.strategy.SimpleChaseStrategy;
import pac.man.model.Level.CollisionCallback;
import pac.man.util.Animation;
import pac.man.util.DimensionF;
import pac.man.util.MathVector;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;

public class GameState {
	public static final int STARTING_LIVES = 3;
	public static final int GOLD_VALUE = 10;
	public static final int POWER_VALUE = 25;
	public static final int GHOST_VALUE = 100;
	public static final int POWERUP_DURATION = 6000;
	public static final int POWERUP_THRESHOLD = 750;
	public static final int GHOST_MOVE_INTERVAL = 175;

	private static enum GameMode {
		NORMAL, POWER_UP
	}

	private final Context context;

	private boolean running = true;
	private int numOpponents = 4;
	private int lives;
	private int score;

	private Level level;
	private Player player;
	private final List<Ghost> ghosts = new ArrayList<Ghost>();

	private long ghostMovementCounter;
	private long modeCounter;
	private long thresholdCounter;
	private GameMode gameMode = GameMode.NORMAL;

	public GameState(Player player, Level level,
			ArrayList<Map<Character.AnimationType, Integer>> ghosts,
			Context context) {
		this.context = context;

		this.level = level;
		this.player = player;

		int size = ghosts.size();

		Integer sampleId = ghosts.get(0).values().iterator().next();
		Animation sampleGhostAnimationFrame = ResourceManager
				.getAnimation(sampleId);
		DimensionF ghostSize = new DimensionF(
				sampleGhostAnimationFrame.getFrameDimension().width,
				sampleGhostAnimationFrame.getFrameDimension().height);

		for (int i = 0; i < size; ++i) {
			this.ghosts.add(new Ghost(ghostSize, new MathVector(-100, 0),
					ghosts.get(i)));
		}

		restartLevel();
	}

	private void handleSpecialInteraction(Ghost ghost) {
		ghost.setAlive(false);
		ghost.setSpeed(new MathVector(0, 0));
		ghost.setMovementStrategy(new RandomStrategy());

		// Slow the ghost.
		ghost.setMovementAlgorithm(new Strict4WayMovement(0.5));

		score += GHOST_VALUE;

		ResourceManager.playSound(context, R.raw.pacman_eatghost);
	}

	private void handleNormalInteraction() {
		lives--;
		ResourceManager.playSound(context, R.raw.death);
		PacMan.showMessage("Life lost");

		if (lives <= 0) {
			player.setAlive(false);
			player.setSpeed(new MathVector(0, 0));
			running = false;
			ResourceManager.playSound(context, R.raw.pacman_death);
		} else {
			resetLevel();
		}
	}

	/*
	 * Player - Ghost collisions
	 */
	private void handleInteractions() {
		Rect playerBoundary = player.getBoundingRect();
		Rect ghostBoundary;
		boolean special = player.isSpecial();

		int nActiveGhosts = Math.min(ghosts.size(), numOpponents);

		for (int i = 0; i < nActiveGhosts; ++i) {
			Ghost ghost = ghosts.get(i);
			if (!ghost.isAlive())
				continue;

			ghostBoundary = ghost.getBoundingRect();

			if (Rect.intersects(playerBoundary, ghostBoundary)) {
				if (special) {
					handleSpecialInteraction(ghost);
				} else {
					handleNormalInteraction();
				}
				// XXX: mozna usunac?
				// break;
			}
		}
	}

	private int getActualGhostNumber() {
		return Math.min(ghosts.size(), numOpponents);
	}

	/**
	 * @return <code>true</code> if at least one ghost is alive
	 */
	private boolean isGhostAlive() {
		for (int i = 0; i < getActualGhostNumber(); ++i) {
			if (ghosts.get(i).isAlive())
				return true;
		}
		return false;
	}

	private void moveGhosts() {
		for (int i = 0; i < getActualGhostNumber(); ++i) {
			if (ghostMovementCounter % GHOST_MOVE_INTERVAL == 0) {
				ghosts.get(i).handleMove();
			}
		}

	}

	private void updateLevel(long dt, Canvas canvas) {
		level.update(dt, canvas, player);
		for (int i = 0; i < getActualGhostNumber(); ++i) {
			level.update(dt, canvas, ghosts.get(i));
		}
	}

	private void updateGhosts(long dt, Canvas canvas) {
		for (int i = 0; i < getActualGhostNumber(); ++i) {
			ghosts.get(i).update(dt, canvas);
		}
	}

	public void update(long dt, Canvas canvas) {
		modeCounter -= dt;

		if (gameMode != GameMode.NORMAL && modeCounter <= 0) {
			setNormalMode();
		}

		if (running) {
			handleInteractions();

			if (level.getTotalGold() == 0 && gameMode == GameMode.NORMAL) {
				setPowerupMode();
				modeCounter = Long.MAX_VALUE; // Infinite powerup mode!
			} else if (!isGhostAlive()) {
				running = false;
			}

			player.update(dt, canvas);

			moveGhosts();

			updateLevel(dt, canvas);
			updateGhosts(dt, canvas);

			ghostMovementCounter += dt;
		} else {
			player.update(dt, canvas);
		}
	}

	public void draw(Canvas canvas) {
		level.draw(canvas);

		// XXX: wtf z "thresholdCounter"?

		if (gameMode == GameMode.NORMAL || (modeCounter > POWERUP_THRESHOLD)
				|| (thresholdCounter % 6 < 3)) {
			for (int i = 0; i < getActualGhostNumber(); ++i) {
				ghosts.get(i).draw(canvas);
			}
		}
		thresholdCounter++;

		player.draw(canvas);
	}

	public boolean isRunning() {
		return running;
	}

	public boolean playerWon() {
		return !running && (lives > 0);
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level l) {
		this.level = l;
		restartLevel();
	}

	private void setNormalMode() {
		gameMode = GameMode.NORMAL;

		player.setMovementAlgorithm(new Strict4WayMovement());
		player.setSpecial(false);

		for (Ghost ghost : ghosts) {
			if (!ghost.isAlive())
				continue;

			ghost.setMovementAlgorithm(new Strict4WayMovement());
			ghost.setMovementStrategy(new SimpleChaseStrategy(player, 150.0,
					1.0));
			ghost.setSpecial(false);
		}

		level.setCollisionHandler(new StickyCollisions());
	}

	private void setPowerupMode() {
		gameMode = GameMode.POWER_UP;

		player.setMovementAlgorithm(new InertialMovement(player.getSpeed(),
				23.0));
		player.setSpecial(true);

		for (Ghost ghost : ghosts) {
			ghost.setMovementAlgorithm(new NonrestrictiveMovement());
			ghost.setMovementStrategy(new SimpleChaseStrategy(player, 150.0,
					-1.0));
			ghost.setSpecial(true);
		}

		level.setCollisionHandler(new BouncyCollisions());

		modeCounter = POWERUP_DURATION;
		ResourceManager.playSound(context, R.raw.pacman_intermission);
	}

	public void restartLevel() {
		resetLevel();

		running = true;
		gameMode = GameMode.NORMAL;
		lives = STARTING_LIVES;
		score = 0;

		player.setSpeed(new MathVector(0, 0));

		for (Ghost ghost : ghosts) {
			ghost.setPosition(level.randomEnemySpawn());
		}

		for (Ghost ghost : ghosts) {
			ghost.setPosition(level.randomEnemySpawn());
		}

		level.init();
		level.setCollisionCallback(new CollisionCallback() {
			public boolean onWall(Character who) {
				if (who == player && gameMode != GameMode.NORMAL) {
					ResourceManager.playSound(context, R.raw.coin);
					return true;
				}
				return false;
			}

			public boolean onPowerup(Character who) {
				if (who == player) {
					ResourceManager.playSound(context, R.raw.pacman_eatfruit);
					score += POWER_VALUE;
					setPowerupMode();
					return true;
				}
				return false;
			}

			public boolean onGold(Character who) {
				if (who == player) {
					score += GOLD_VALUE;
					ResourceManager.playSound(context, R.raw.pacman_chomp);
					return true;
				}
				return false;
			}
		});
	}

	public void resetLevel() {
		player.setPosition(level.randomPlayerSpawn());
		player.setAlive(true);
		player.setSpecial(false);

		for (Ghost ghost : ghosts) {
			ghost.setAlive(true);
			ghost.setSpecial(false);
		}

		setNormalMode();
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getNumOpponents() {
		return numOpponents;
	}

	public void setNumOpponents(int numOpponents) {
		this.numOpponents = numOpponents;
	}

	public long getPowerupTime() {
		if (gameMode == GameMode.NORMAL)
			return 0;
		else
			return modeCounter;
	}
}
