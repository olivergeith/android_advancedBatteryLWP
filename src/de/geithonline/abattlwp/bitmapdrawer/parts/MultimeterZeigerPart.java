package de.geithonline.abattlwp.bitmapdrawer.parts;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import de.geithonline.abattlwp.bitmapdrawer.data.DropShadow;
import de.geithonline.abattlwp.bitmapdrawer.data.Gradient;
import de.geithonline.abattlwp.bitmapdrawer.data.Outline;
import de.geithonline.abattlwp.bitmapdrawer.shapes.ZeigerShapePath;
import de.geithonline.abattlwp.bitmapdrawer.shapes.ZeigerShapePath.ZEIGER_TYP;
import de.geithonline.abattlwp.settings.PaintProvider;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.Shader;

public class MultimeterZeigerPart {

	private final PointF c;
	private final float ra;
	private final float ri;
	private final Paint paint;
	private final float sweep;
	private final float startWinkel;
	private float dicke = 2f;
	private Outline outline = null;
	private ZEIGER_TYP zeigerType = ZEIGER_TYP.rect;
	private float value;
	private Gradient gradient;
	private final float minValue;
	private final float maxValue;

	public MultimeterZeigerPart(final PointF center, final float value, //
			final float minValue, final float maxValue, //
			final float radAussen, final float radInnen, //
			final float startWinkel, final float sweep) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.sweep = sweep;
		this.startWinkel = startWinkel;
		this.value = value;
		if (value > maxValue) {
			this.value = maxValue;
		}
		if (value < minValue) {
			this.value = minValue;
		}

		c = center;
		ra = radAussen;
		ri = radInnen;

		paint = PaintProvider.getZeigerPaint();
		initPaint();
	}

	public static MultimeterZeigerPart getDefaultVoltmeterPart(//
			final PointF center, final float value, //
			final float radAussen, final float radInnen, //
			final float startWinkel, final float sweep) {
		return new MultimeterZeigerPart(center, value, 3.5f, 4.5f, radAussen, radInnen, startWinkel, sweep);
	}

	public static MultimeterZeigerPart getDefaultThemometerPart(//
			final PointF center, final float value, //
			final float radAussen, final float radInnen, //
			final float startWinkel, final float sweep) {
		return new MultimeterZeigerPart(center, value, 0f, 60f, radAussen, radInnen, startWinkel, sweep);
	}

	private void initPaint() {
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL);
	}

	public MultimeterZeigerPart setDicke(final float dicke) {
		this.dicke = dicke;
		return this;
	}

	public MultimeterZeigerPart setZeigerType(final ZEIGER_TYP zeigerType) {
		this.zeigerType = zeigerType;
		return this;
	}

	public MultimeterZeigerPart overrideColor(final int color) {
		paint.setColor(color);
		return this;
	}

	public MultimeterZeigerPart setGradient(final Gradient gradient) {
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

	public MultimeterZeigerPart setOutline(final Outline outline) {
		this.outline = outline;
		return this;
	}

	public MultimeterZeigerPart setDropShadow(final DropShadow dropShadow) {
		if (dropShadow != null) {
			dropShadow.setUpPaint(paint);
		}
		return this;
	}

	public void draw(final Canvas canvas) {
		final float scaleDiff = maxValue - minValue;
		final float valueDiff = value - minValue;
		final float valueSweep = sweep / scaleDiff * valueDiff;
		final float winkel = startWinkel + valueSweep;
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
