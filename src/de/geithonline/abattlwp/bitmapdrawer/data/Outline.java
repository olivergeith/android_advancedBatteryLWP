package de.geithonline.abattlwp.bitmapdrawer.data;

public class Outline {

	private final int color;
	private final float strokeWidth;

	public Outline(final int color, final float strokeWidth) {
		this.color = color;
		this.strokeWidth = strokeWidth;
	}

	public int getColor() {
		return color;
	}

	public float getStrokeWidth() {
		return strokeWidth;
	}

}
