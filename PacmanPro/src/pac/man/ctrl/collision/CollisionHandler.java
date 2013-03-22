package pac.man.ctrl.collision;

import pac.man.model.Character;
import pac.man.util.Dimension;
import android.graphics.Rect;

public interface CollisionHandler {
	void handle(long dt, Dimension canvasDimension, Rect with, Character who);
}