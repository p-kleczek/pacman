package pac.man.model;

import java.util.ArrayList;
import java.util.Iterator;
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
import pac.man.model.level.Level;
import pac.man.model.level.Level.CollisionCallback;
import pac.man.util.Animation;
import pac.man.util.Dimension;
import pac.man.util.MathVector;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;

public class GameState {
	public static final int STARTING_LIVES_NUMBER = 3;
	public static final int GOLD_VALUE = 10;
	public static final int POWER_VALUE = 25;
	public static final int GHOST_VALUE = 100;
	public static final int POWERUP_DURATION = 6000;
	public static final int POWERUP_THRESHOLD = 750;
	public static final int GHOST_MOVE_INTERVAL = 175;

	private static final double SLOW_SPEED_FACTOR = 0.5;

	private final CollisionCallback collisionCallback = new CollisionCallback() {
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
	};

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
	private final List<Ghost> activeGhosts = new ArrayList<Ghost>();
	
	// Ghost not currently used due to selected preferences.
	private final List<Ghost> ghostRepo = new ArrayList<Ghost>();

	/**
	 * Measures the total time of the game.
	 */
	private long globalTimer;

	private long modeTimer;

	private GameMode gameMode = GameMode.NORMAL;

	public GameState(Player player, Level level,
			ArrayList<Map<Character.AnimationType, Integer>> ghosts,
			Context context) {
		this.context = context;

		this.level = level;
		this.player = player;

		Integer sampleId = ghosts.get(0).values().iterator().next();
		Animation sampleGhostAnimationFrame = ResourceManager
				.getAnimation(sampleId);
		Dimension ghostSize = new Dimension(
				sampleGhostAnimationFrame.getFrameDimension().width,
				sampleGhostAnimationFrame.getFrameDimension().height);

		for (int i = 0; i < ghosts.size(); ++i) {
			this.activeGhosts.add(new Ghost(ghostSize, new MathVector(-100, 0),
					ghosts.get(i)));
		}

		restartLevel();
	}

	private void handleSpecialInteraction(Ghost ghost) {
		ghost.setAlive(false);
		ghost.setSpeed(new MathVector());
		ghost.setMovementStrategy(new RandomStrategy());

		ghost.setMovementAlgorithm(new Strict4WayMovement(SLOW_SPEED_FACTOR));

		score += GHOST_VALUE;

		ResourceManager.playSound(context, R.raw.pacman_eatghost);
	}

	private void handleNormalInteraction() {
		lives--;
		ResourceManager.playSound(context, R.raw.death);
		PacMan.showMessage("Life lost");

		if (lives <= 0) {
			player.setAlive(false);
			player.setSpeed(new MathVector());
			running = false;
			ResourceManager.playSound(context, R.raw.pacman_death);
		} else {
			resetCharactersAndMode();
		}
	}

	/*
	 * Player - Ghost collisions
	 */
	private void handleInteractions() {
		Rect playerBoundary = player.getBoundingRect();
		Rect ghostBoundary;
		Ghost ghost;

		int nActiveGhosts = Math.min(activeGhosts.size(), numOpponents);

		for (int i = 0; i < nActiveGhosts; i++) {
			ghost = activeGhosts.get(i);

			if (!ghost.isAlive())
				continue;

			ghostBoundary = ghost.getBoundingRect();

			if (Rect.intersects(playerBoundary, ghostBoundary)) {
				if (player.isSpecial()) {
					handleSpecialInteraction(ghost);
				} else {
					handleNormalInteraction();
				}

				break;
			}
		}
	}

	/**
	 * @return <code>true</code> if at least one ghost is alive
	 */
	private boolean isGhostAlive() {
		for (Ghost g : activeGhosts) {
			if (g.isAlive())
				return true;
		}
		return false;
	}

	private void moveGhosts() {
		for (Ghost g : activeGhosts) {
			if (globalTimer % GHOST_MOVE_INTERVAL == 0) {
				g.handleMove();
			}
		}

	}

	private void updateLevel(long timeInterval, Dimension canvasDimension) {
		level.update(timeInterval, canvasDimension, player);
		for (Ghost ghost : activeGhosts) {
			level.update(timeInterval, canvasDimension, ghost);
		}
	}

	private void updateGhosts(long timeInterval, Dimension canvasDimension) {
		for (Ghost g : activeGhosts) {
			g.update(timeInterval, canvasDimension);
		}
	}

	public void update(long timeInterval, Dimension canvasDimension) {
		modeTimer -= timeInterval;

		if (gameMode != GameMode.NORMAL && modeTimer <= 0) {
			setNormalMode();
		}

		if (running) {
			handleInteractions();

			if (level.getTotalGold() == 0 && gameMode == GameMode.NORMAL) {
				setPowerupMode();
				modeTimer = Long.MAX_VALUE; // Infinite powerup mode!
			} else if (!isGhostAlive()) {
				running = false;
			}

			player.update(timeInterval, canvasDimension);

			moveGhosts();

			updateLevel(timeInterval, canvasDimension);
			updateGhosts(timeInterval, canvasDimension);

			globalTimer += timeInterval;
		} else {
			player.update(timeInterval, canvasDimension);
		}
	}

	public void draw(Canvas canvas) {
		level.draw(canvas);

		if (gameMode == GameMode.NORMAL || modeTimer > POWERUP_THRESHOLD) {
			for (Ghost g : activeGhosts) {
				g.draw(canvas);
			}
		}

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

		// FIXME: to nie powinno się tu znaleźc
		restartLevel();
	}

	private void setNormalMode() {
		gameMode = GameMode.NORMAL;

		player.setMovementAlgorithm(new Strict4WayMovement());
		player.setSpecial(false);

		for (Ghost ghost : activeGhosts) {
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

		for (Ghost ghost : activeGhosts) {
			ghost.setMovementAlgorithm(new NonrestrictiveMovement());
			ghost.setMovementStrategy(new SimpleChaseStrategy(player, 150.0,
					-1.0));
			ghost.setSpecial(true);
		}

		level.setCollisionHandler(new BouncyCollisions());

		modeTimer = POWERUP_DURATION;
		ResourceManager.playSound(context, R.raw.pacman_intermission);
	}

	public void restartLevel() {
		level.setCollisionCallback(collisionCallback);
		
		// Necessary to restore eg. eaten coins.
		level.init();

		resetCharactersAndMode();

		running = true;
		lives = STARTING_LIVES_NUMBER;
		score = 0;
	}

	private void resetCharactersAndMode() {
		player.setPosition(level.getRandomPlayerSpawnPosition());
		player.setAlive(true);
		player.setSpecial(false);
		player.setSpeed(new MathVector());

		for (Ghost ghost : activeGhosts) {
			ghost.setPosition(level.getRandomEnemySpawnPosition());
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
		
		if (activeGhosts.size() > numOpponents) {
			Iterator<Ghost> iterator = activeGhosts.iterator();
			
			while (activeGhosts.size() > numOpponents) {
				ghostRepo.add(iterator.next());
				iterator.remove();
			}
		}
		
		if (activeGhosts.size() < numOpponents) {
			Iterator<Ghost> iterator = ghostRepo.iterator();
			
			while (activeGhosts.size() < numOpponents) {
				activeGhosts.add(iterator.next());
				iterator.remove();
			}
		}		
	}

}
