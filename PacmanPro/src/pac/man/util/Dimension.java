package pac.man.util;

public class Dimension {
	public final int width;
	public final int height;

	public Dimension(int width, int height) {
		super();
		this.width = width;
		this.height = height;
	}

	public Dimension(Dimension dim) {
		width = dim.width;
		height = dim.height;
	}
}
