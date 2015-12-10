package de.geithonline.abattlwp.bitmapdrawer.parts;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import de.geithonline.abattlwp.bitmapdrawer.shapes.ZeigerShapePath;
import de.geithonline.abattlwp.bitmapdrawer.shapes.ZeigerShapePath.ZEIGER_TYP;
import de.geithonline.abattlwp.settings.PaintProvider;

public class SkalaLinePart {

	private final PointF c;
	private final float ra10er;
	private float ra5er = 0f;
	private float ra1er = 0f;
	private final float ri10er;
	private float ri5er;

	private float ri1er;
	private final Paint paint;
	private boolean draw100 = false;
	private boolean draw0 = true;

	private final float maxWinkel;
	private final float startWinkel;
	private float dicke10er = 2f;
	private float dicke1er = 2f;
	private float dicke5er = 2f;

	public SkalaLinePart(final PointF center, final float rad10er, final float radInnen, final float startWinkel, final float maxWinkel) {
		c = center;
		ra1er = 0f;
		ra5er = 0f;
		ra10er = rad10er;
		ri10er = radInnen;
		ri1er = radInnen;
		ri5er = radInnen;
		paint = PaintProvider.initScalePaint();

		this.maxWinkel = maxWinkel;
		this.startWinkel = startWinkel;

		initPaint();
	}

	private void initPaint() {
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL);
	}

	public SkalaLinePart setDraw100(final boolean draw100) {
		this.draw100 = draw100;
		return this;
	}

	public SkalaLinePart setDraw0(final boolean draw0) {
		this.draw0 = draw0;
		return this;
	}

	/**
	 * @param ra1er
	 *            Auﬂenradius 1er Striche <br>
	 *            if set to 0f no 1eer Striche will be painted
	 * @return
	 */
	public SkalaLinePart set1erRadiusAussen(final float ra1er) {
		this.ra1er = ra1er;
		return this;
	}

	/**
	 * @param ra5er
	 *            Auﬂenradius 5er Striche <br>
	 *            if set to 0f...no 5er striche will be painted
	 * @return
	 */
	public SkalaLinePart set5erRadiusAussen(final float ra5er) {
		this.ra5er = ra5er;
		return this;
	}

	public SkalaLinePart set5erRadiusInnen(final float ri5er) {
		this.ri5er = ri5er;
		return this;
	}

	public SkalaLinePart set1erRadiusInnen(final float ri1er) {
		this.ri1er = ri1er;
		return this;
	}

	public SkalaLinePart setDicke10er(final float dicke10er) {
		this.dicke10er = dicke10er;
		return this;
	}

	public SkalaLinePart setDicke1er(final float dicke1er) {
		this.dicke1er = dicke1er;
		return this;
	}

	public SkalaLinePart setDicke5er(final float dicke5er) {
		this.dicke5er = dicke5er;
		return this;
	}

	/**
	 * @param dicke
	 *            setzt die Dicke auf alle linien gleich
	 * @return
	 */
	public SkalaLinePart setDicke(final float dicke) {
		dicke1er = dicke;
		dicke10er = dicke;
		dicke5er = dicke;
		return this;
	}

	public void draw(final Canvas canvas) {
		float ra = 0f;
		float ri = 0f;
		float dicke = 2f;
		int forloopEnd = 100;
		if (draw100) {
			forloopEnd = 101;
		}
		int forloopStart = 1;
		if (draw0) {
			forloopStart = 0;
		}
		for (int i = forloopStart; i < forloopEnd; i = i + 1) {
			if (i % 10 == 0) {// 10 er
				ra = ra10er;
				ri = ri10er;
				dicke = dicke10er;
			} else if (i % 5 == 0) { // 5er
				ra = ra5er;
				ri = ri5er;
				dicke = dicke5er;
			} else { // einer
				ra = ra1er;
				ri = ri1er;
				dicke = dicke1er;
			}
			if (ra > 0f) {
				final float winkelProProzent = maxWinkel / 100;
				final float winkel = startWinkel + i * winkelProProzent;
				final Path path = new ZeigerShapePath(c, ra, ri, dicke, winkel, ZEIGER_TYP.rect);
				canvas.drawPath(path, paint);
			}
		}
	}

}
