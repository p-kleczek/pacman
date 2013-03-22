package pac.man.model.level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import pac.man.ctrl.collision.CollisionHandler;
import pac.man.ctrl.collision.StickyCollisions;
import pac.man.model.Character;
import pac.man.model.Drawable;
import pac.man.util.Dimension;
import pac.man.util.MathVector;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

// TODO Random level generator.
public class Level implements Drawable {

	private enum FieldType {
		WALL(0x000000), ENEMY_SPAWN(0xff0000), PLAYER_SPAWN(0x00ff00), POWER_SPAWN(
				0x0000ff), GOLD_SPAWN(0xffff00);

		private static final int RGB_MASK = 0x00ffffff;

		private final int pixelValue;

		private FieldType(int pixelValue) {
			this.pixelValue = pixelValue;
		}

		public static FieldType valueOf(int argbPixelValue) {
			for (FieldType f : FieldType.values()) {
				if (f.pixelValue == (argbPixelValue & RGB_MASK))
					return f;
			}
			throw new IllegalArgumentException(String.valueOf(argbPixelValue));
		}
	}

	public interface CollisionCallback {
		public boolean onWall(Character who);

		public boolean onGold(Character who);

		public boolean onPowerup(Character who);
	}

	private final int blockSize;
	private Dimension boardDimension;

	// pola, z ktorymi moze wystapic kolizja
	private final List<Field> collidableFields;

	private final List<Rect> enemySpawns;
	private final List<Rect> playerSpawns;

	private CollisionHandler collisionHandler;
	private CollisionCallback collisionCallback;

	private Random random = new Random(0);

	private Bitmap layout;

	public Level(Bitmap layout, Dimension displayDimension) {
		// TODO Rect merging.

		this.layout = layout;

		boardDimension = new Dimension(layout.getWidth(), layout.getHeight());

		blockSize = (int) Math.min((double) displayDimension.width
				/ (boardDimension.width - 1), (double) displayDimension.height
				/ (boardDimension.height - 1));

		collidableFields = Collections.synchronizedList(new ArrayList<Field>());
		playerSpawns = Collections.synchronizedList(new ArrayList<Rect>());
		enemySpawns = Collections.synchronizedList(new ArrayList<Rect>());

		// Defaults to sticky collisions.
		collisionHandler = new StickyCollisions();

		init();
	}

	public synchronized void init() {
		collidableFields.clear();
		playerSpawns.clear();
		enemySpawns.clear();

		Point pos = new Point();
		for (int h = 0; h < boardDimension.height; ++h) {
			for (int w = 0; w < boardDimension.width; ++w) {
				pos.set(w, h);
				Rect r = new Rect(w * blockSize, h * blockSize, (w + 1)
						* blockSize, (h + 1) * blockSize);

				int pixelValue = layout.getPixel(w, h);

				try {
					FieldType fieldType = FieldType.valueOf(pixelValue);

					switch (fieldType) {
					case WALL:
						collidableFields.add(new Block(pos, blockSize,
								collisionHandler, collisionCallback));
						break;
					case ENEMY_SPAWN:
						enemySpawns.add(r);
						break;
					case PLAYER_SPAWN:
						playerSpawns.add(r);
						break;
					case POWER_SPAWN:
						collidableFields.add(new PowerSpawn(pos, blockSize,
								collisionHandler, collisionCallback));
						break;
					case GOLD_SPAWN:
						collidableFields.add(new GoldSpawn(pos, blockSize,
								collisionHandler, collisionCallback));
						break;
					}
				} catch (Exception e) {
//					System.err.println(String.format(
//							"Undefined pixel value (%d)", pixelValue));
				}
			}
		}
	}

	private void processCollisions(long timeInterval,
			Dimension canvasDimension, Character character) {
		Iterator<Field> iterator = collidableFields.iterator();
		while (iterator.hasNext()) {
			Field field = iterator.next();
			if (Rect.intersects(character.getBoundingRect(), field.getRect())) {
				field.handleCollision(timeInterval, canvasDimension, character,
						iterator);
				break;
			}
		}
	}

	public synchronized void update(long timeInterval,
			Dimension canvasDimension, Character character) {
		PowerSpawn.updateAnimationExecutor(timeInterval);
		GoldSpawn.updateAnimationExecutor(timeInterval);

		if (!character.isMoving())
			return;

		processCollisions(timeInterval, canvasDimension, character);
	}

	public synchronized void draw(Canvas canvas) {
		for (Drawable d : collidableFields) {
			d.draw(canvas);
		}
	}

	public MathVector getRandomPlayerSpawnPosition() {
		if (!playerSpawns.isEmpty()) {
			int randomSpawnIndex = random.nextInt(playerSpawns.size());
			Rect r = playerSpawns.get(randomSpawnIndex);
			return new MathVector(r.left, r.top);
		} else {
			return new MathVector();
		}
	}

	public MathVector getRandomEnemySpawnPosition() {
		if (!enemySpawns.isEmpty()) {
			int randomSpawnIndex = random.nextInt(enemySpawns.size());
			Rect r = enemySpawns.get(randomSpawnIndex);
			return new MathVector(r.left, r.top);
		} else {
			return new MathVector();
		}
	}

	public void setCollisionHandler(CollisionHandler c) {
		collisionHandler = c;
	}

	public void setCollisionCallback(CollisionCallback cc) {
		collisionCallback = cc;
	}

	// TODO : zmienic nazwe
	public int getTotalGold() {
		int coinsCounter = 0;
		int powerupCounter = 0;

		for (Field f : collidableFields) {
			if (f instanceof GoldSpawn)
				coinsCounter++;
			else if (f instanceof PowerSpawn)
				powerupCounter++;
		}

		return coinsCounter + powerupCounter;
	}
}
