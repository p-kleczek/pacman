package pac.man;

import pac.man.model.level.Level;
import pac.man.util.Animation;
import pac.man.util.Dimension;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.View;

public class ResourceManager {
	private static SoundPool soundPool = new SoundPool(10,
			AudioManager.STREAM_MUSIC, 0);
	private static SparseIntArray sounds = new SparseIntArray();
	private static SparseBooleanArray soundLoaded = new SparseBooleanArray();
	private static SparseArray<Animation> animations = new SparseArray<Animation>();

	static {
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleID,
					int status) {
				soundLoaded.put(sampleID, true);
			}
		});
	}

	public static Level getLevel(View view, int id) {
		return new Level(BitmapFactory.decodeResource(view.getResources(), id),
				new Dimension(view.getWidth(), view.getHeight()));
	}

	public static void loadAnimation(View view, int id, int frames, int duration) {
		Animation animation = new Animation(BitmapFactory.decodeResource(
				view.getResources(), id), frames, duration);
		Integer key = Integer.valueOf(id);
		animations.put(key, animation);
	}

	public static Animation getAnimation(Integer id) {
		if (animations.get(id) != null) {
			return animations.get(id);
		} else {
			throw new IllegalArgumentException(String.valueOf(id));
		}
	}

	public static void loadSound(Context context, int id) {
		sounds.put(id, soundPool.load(context, id, 1));
	}

	public static void playSound(Context context, int id) {
		// Getting the user sound settings
		AudioManager audioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		float actualVolume = (float) audioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		float maxVolume = (float) audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume = actualVolume / maxVolume;

		if (sounds.get(id) != 0) {
			int sound = sounds.get(id);
			boolean loaded = soundLoaded.get(sound);

			if (loaded) {
				soundPool.play(sound, volume, volume, 1, 0, 1f);
			}
		}
	}
}