package pac.man.ctrl.collision;

import android.graphics.Rect;
import android.graphics.Canvas;

import pac.man.model.Character;
import pac.man.util.DimensionI;

public interface CollisionHandler {
	void handle(long dt, DimensionI canvasDimension, Rect with, Character who);
}