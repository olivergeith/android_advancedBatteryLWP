package de.geithonline.abattlwp.bitmapdrawer.parts;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import de.geithonline.abattlwp.bitmapdrawer.data.DropShadow;
import de.geithonline.abattlwp.bitmapdrawer.data.Gradient;
import de.geithonline.abattlwp.bitmapdrawer.data.Outline;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZMode;
import de.geithonline.abattlwp.bitmapdrawer.shapes.ZeigerShapePath;
import de.geithonline.abattlwp.bitmapdrawer.shapes.ZeigerShapePath.ZEIGER_TYP;
import de.geithonline.abattlwp.settings.PaintProvider;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.Shader;

public class ZeigerPart {

	private final PointF c;
	private final float ra;
	private final float ri;
	private final Paint paint;
	private final int levelIntern;
	private final float maxWinkel;
	private final float startWinkel;
	private final float dicke;
	private Outline outline = null;
	private ZEIGER_TYP zeigerType = ZEIGER_TYP.rect;
	private final EZMode modus;
	private int anzahlLevel;
	private final int level;
	private Gradient gradient;

	public ZeigerPart(final PointF center, final int level, final float radAussen, final float radInnen, final float dicke, final float startWinkel,
			final float maxWinkel, final EZMode modus) {
		this.dicke = dicke;
		this.modus = modus;
		this.level = level;
		this.maxWinkel = maxWinkel;
		this.startWinkel = startWinkel;
		c = center;
		ra = radAussen;
		ri = radInnen;
		switch (this.modus) {
		default:
		case Einer:
			levelIntern = this.level;
			anzahlLevel = 100;
			break;
		case EinerOnly9Segmente:
			levelIntern = this.level % 10;
			anzahlLevel = 9;
			break;
		case EinerOnly10Segmente:
			levelIntern = this.level % 10;
			anzahlLevel = 10;
			break;
		case Fuenfer:
			levelIntern = level / 5;
			anzahlLevel = 20;
			break;
		case Zehner:
			levelIntern = this.level / 10;
			anzahlLevel = 10;
			break;
		}

		paint = PaintProvider.getZeigerPaint();
		initPaint();
	}

	private void initPaint() {
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL);
	}

	public ZeigerPart setZeigerType(final ZEIGER_TYP zeigerType) {
		this.zeigerType = zeigerType;
		return this;
	}

	public ZeigerPart overrideColor(final int color) {
		paint.setColor(color);
		return this;
	}

	public ZeigerPart setGradient(final Gradient gradient) {
		this.gradient = gradient;
		setupGradient();
		return this;
	}

	private void setupGradient() {
		if (gradient != null) {
			paint.setStyle(Style.FILL);
			switch (gradient.getStyle()) {
			default:
			case top2bottom:
				paint.setShader(new LinearGradient(c.x, c.y - ra, c.x, c.y + ra, gradient.getColor1(), gradient.getColor2(), Shader.TileMode.MIRROR));
				break;
			case radial:
				final int[] colors = new int[] { gradient.getColor1(), gradient.getColor2() };
				paint.setShader(new RadialGradient(c.x, c.y, ra, colors, getDistancesRadial(), Shader.TileMode.CLAMP));
				break;
			}
		}
	}

	private float[] getDistancesRadial() {
		final float di = ri / ra;
		final float da = 1.0f;
		final float distances[] = new float[] { di, da };
		return distances;
	}

	public ZeigerPart setOutline(final Outline outline) {
		this.outline = outline;
		return this;
	}

	public ZeigerPart setDropShadow(final DropShadow dropShadow) {
		if (dropShadow != null) {
			dropShadow.setUpPaint(paint);
		}
		return this;
	}

	public void draw(final Canvas canvas) {
		final float winkelProlevel = maxWinkel / anzahlLevel;
		final float winkel = startWinkel + levelIntern * winkelProlevel;
		final Path path = new ZeigerShapePath(c, ra, ri, dicke, winkel, zeigerType);
		canvas.drawPath(path, paint);
		if (outline != null) {
			paint.setShader(null);
			paint.setShadowLayer(0, 0, 0, Color.BLACK);
			paint.setColor(outline.getColor());
			// paint.setAlpha(255);
			paint.setStrokeWidth(outline.getStrokeWidth());
			paint.setStyle(Style.STROKE);
			canvas.drawPath(path, paint);
		}
	}

}
