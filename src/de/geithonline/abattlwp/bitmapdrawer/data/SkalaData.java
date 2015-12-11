package de.geithonline.abattlwp.bitmapdrawer.data;

public class SkalaData {
	public float scalaSweep;
	public float startWinkel;
	public float minValue;
	public float maxValue;

	public SkalaData(final float startWinkel, final float scalaSweep, final float minValue, final float maxValue) {
		super();
		this.scalaSweep = scalaSweep;
		this.startWinkel = startWinkel;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
}
