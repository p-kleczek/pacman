package pac.man.util;

public class DimensionI {
	public final int width;
	public final int height;

	public DimensionI(int width, int height) {
		super();
		this.width = width;
		this.height = height;
	}

	public DimensionI(DimensionI dim) {
		width = dim.width;
		height = dim.height;
	}
}
