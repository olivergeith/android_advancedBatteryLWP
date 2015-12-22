package de.geithonline.abattlwp.bitmapdrawer.shapes;

import android.graphics.Path;
import android.graphics.PointF;
import de.geithonline.abattlwp.utils.PathHelper;

/**
 * @author Oliver
 *
 */
public class RoatingPatternsPath extends Path {

	/**
	 * @param arms
	 * @param center
	 * @param ra
	 * @param rotate
	 *            in grad
	 * @param type
	 */
	public RoatingPatternsPath(final int arms, final PointF center, final float ra, final float rotate, final String type) {
		switch (type) {
			default:
				drawRotatingPattern(arms, center, ra, rotate, type);
				break;
			case "10 Circles":
				draw10Circles(center, ra, rotate);
				break;
		}
	}

	private void drawRotatingPattern(final int arms, final PointF center, final float ra, final float rotate, final String type) {
		final float rotateWinkel = (float) (rotate / 360 * 2 * Math.PI);
		final float winkelProArm = (float) (2 * Math.PI / (arms));
		final float da = 2 * ra;
		final float dCircle = (float) ((Math.PI * da) / (arms + 2 * Math.PI));
		final float flameCenterRadius = ra - dCircle / 2; // ra * 0.78f;
		final float flameSize = dCircle / 2; // ra * 0.23f;
		for (int i = 0; i < arms; i++) {
			final int winkelInGrad = (int) ((i * winkelProArm + rotateWinkel) * 360 / (2 * Math.PI));
			final PointF p = new PointF();
			p.x = (int) (center.x + Math.cos((i) * winkelProArm + rotateWinkel) * flameCenterRadius);
			p.y = (int) (center.y + Math.sin((i) * winkelProArm + rotateWinkel) * flameCenterRadius);
			final Path path;
			switch (type) {
				default:
				case "Circle":
					path = new CirclePath(p, flameSize, 0f);
					break;
				case "Drop":
					path = new DropPath(p, flameSize);
					break;
				case "Heart":
					path = new HeartPath(p, flameSize);
					break;
			}
			PathHelper.rotatePath(p.x, p.y, path, winkelInGrad + 90);
			addPath(path);
		}
	}

	private void draw10Circles(final PointF center, final float ra, final float rotate) {
		final int arms = 10;
		final float winkelProArm = (float) (2 * Math.PI / (arms));
		final float flameCenterRadius = ra * 0.78f;
		final float flameSize = ra * 0.23f;
		for (int i = 0; i < arms; i++) {
			final int winkelInGrad = (int) ((i * winkelProArm) * 360 / (2 * Math.PI));
			final PointF p = new PointF();
			p.x = (int) (center.x + Math.cos((i) * winkelProArm) * flameCenterRadius);
			p.y = (int) (center.y + Math.sin((i) * winkelProArm) * flameCenterRadius);
			final Path flame = new CirclePath(p, flameSize, 0f);
			PathHelper.rotatePath(p.x, p.y, flame, winkelInGrad + 90);
			addPath(flame);
		}
	}

}
