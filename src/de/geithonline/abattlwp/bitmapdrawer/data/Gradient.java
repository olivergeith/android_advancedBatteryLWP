package de.geithonline.abattlwp.bitmapdrawer.data;

public class Gradient {

	public enum GRAD_STYLE {
		radial, top2bottom, diagonal;
	}

	private final int color1;
	private final int color2;
	private final GRAD_STYLE style;

	public Gradient(final int color1, final int color2, final GRAD_STYLE style) {
		this.color1 = color1;
		this.color2 = color2;
		this.style = style;
	}

	public int getColor1() {
		return color1;
	}

	public int getColor2() {
		return color2;
	}

	public GRAD_STYLE getStyle() {
		return style;
	}

}
