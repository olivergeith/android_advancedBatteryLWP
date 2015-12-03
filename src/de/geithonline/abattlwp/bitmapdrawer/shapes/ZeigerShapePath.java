package de.geithonline.abattlwp.bitmapdrawer.shapes;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import de.geithonline.abattlwp.utils.PathHelper;

public class ZeigerShapePath extends Path {

	public enum ZEIGER_TYP {
		triangle, circle, rect, raute, needle, inward_triangle;
	}

	public ZeigerShapePath(final PointF center, final float ra, final float ri, final float size, final float rotateWinkel, final ZEIGER_TYP typ) {

		switch (typ) {
		default:
		case triangle:
			drawTriangle(center, ra, ri, size, rotateWinkel);
			break;
		case inward_triangle:
			drawInWardTriangle(center, ra, ri, size, rotateWinkel);
			break;
		case circle:
			drawCircle(center, ra, ri, size, rotateWinkel);
			break;
		case rect:
			drawRect(center, ra, ri, size, rotateWinkel);
			break;
		case raute:
			drawRaute(center, ra, ri, size, rotateWinkel);
			break;
		case needle:
			drawNeedle(center, ra, ri, size, rotateWinkel);
			break;

		}
	}

	private void drawNeedle(final PointF center, final float ra, final float ri, final float size, final float rotateWinkel) {
		final Path p = new Path();
		final float rm = ri + (ra - ri) / 2;
		p.moveTo(center.x + ra, center.y);
		p.lineTo(center.x + rm, center.y + size / 2);
		p.lineTo(center.x + ri, center.y + size / 2);
		p.lineTo(center.x + ri, center.y - size / 2);
		p.lineTo(center.x + rm, center.y - size / 2);
		p.close();
		PathHelper.rotatePath(center.x, center.y, p, rotateWinkel);
		addPath(p);
	}

	private void drawRaute(final PointF center, final float ra, final float ri, final float size, final float rotateWinkel) {
		final Path p = new Path();
		final float rm = ri + (ra - ri) / 2;
		p.moveTo(center.x + ra, center.y);
		p.lineTo(center.x + rm, center.y + size / 2);
		p.lineTo(center.x + ri, center.y);
		p.lineTo(center.x + rm, center.y - size / 2);
		p.close();
		PathHelper.rotatePath(center.x, center.y, p, rotateWinkel);
		addPath(p);

	}

	private void drawRect(final PointF center, final float ra, final float ri, final float size, final float rotateWinkel) {
		final Path p = new Path();
		final RectF zeiger = new RectF();
		zeiger.left = center.x + ri;
		zeiger.right = center.x + ra;
		zeiger.top = center.y - size / 2;
		zeiger.bottom = center.y + size / 2;
		p.addRect(zeiger, Direction.CW);
		PathHelper.rotatePath(center.x, center.y, p, rotateWinkel);
		addPath(p);
	}

	private void drawTriangle(final PointF center, final float ra, final float ri, final float size, final float rotateWinkel) {
		final Path p = new Path();
		p.moveTo(center.x + ra, center.y);
		p.lineTo(center.x + ri, center.y + size / 2);
		p.lineTo(center.x + ri, center.y - size / 2);
		p.close();
		PathHelper.rotatePath(center.x, center.y, p, rotateWinkel);
		addPath(p);
	}

	private void drawCircle(final PointF center, final float ra, final float ri, final float size, final float rotateWinkel) {
		final Path p = new Path();
		final RectF zeiger = new RectF();
		zeiger.left = center.x + ri;
		zeiger.right = center.x + ra;
		zeiger.top = center.y - size / 2;
		zeiger.bottom = center.y + size / 2;
		p.addOval(zeiger, Direction.CW);
		PathHelper.rotatePath(center.x, center.y, p, rotateWinkel);
		addPath(p);
	}

	private void drawInWardTriangle(final PointF center, final float ra, final float ri, final float size, final float rotateWinkel) {

		final Path p = new Path();
		p.moveTo(center.x + ri, center.y);
		final RectF oval = new RectF();
		oval.left = center.x - ra;
		oval.right = center.x + ra;
		oval.top = center.y - ra;
		oval.bottom = center.y + ra;
		p.arcTo(oval, -size / 2, size);
		p.close();
		PathHelper.rotatePath(center.x, center.y, p, rotateWinkel);
		addPath(p);
	}

}
