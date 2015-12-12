package de.geithonline.abattlwp.bitmapdrawer.data;

public class RadiusData {
	public final float ra;
	public final float ri;
	public float distance;

	public RadiusData(final float ra, final float ri) {
		this.ra = ra;
		this.ri = ri;
		distance = ra - ri;
	}

}
