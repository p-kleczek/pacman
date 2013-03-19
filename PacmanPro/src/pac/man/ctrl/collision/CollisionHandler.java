package pac.man.ctrl.collision;

import android.graphics.Rect;
import android.graphics.Canvas;

import pac.man.model.Character;

public interface CollisionHandler {
	void handle(long dt, Canvas canvas, Rect with, Character who);
}