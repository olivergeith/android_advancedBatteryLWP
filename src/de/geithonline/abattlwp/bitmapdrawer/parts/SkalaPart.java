package de.geithonline.abattlwp.bitmapdrawer.parts;

import java.util.Locale;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import de.geithonline.abattlwp.bitmapdrawer.data.FontAttributes;
import de.geithonline.abattlwp.bitmapdrawer.shapes.ZeigerShapePath;
import de.geithonline.abattlwp.bitmapdrawer.shapes.ZeigerShapePath.ZEIGER_TYP;
import de.geithonline.abattlwp.settings.PaintProvider;
import de.geithonline.abattlwp.utils.GeometrieHelper;

public class SkalaPart {

	private final PointF c;
	/**
	 * der Hauptradius, an dem sich alles anschmiegt
	 */
	private float radiusMain;
	private float radiusLineEbene1;
	private float radiusLineEbene2;
	private float radiusLineEbene3;

	private float radiusBaseLine = 0f;
	private float dickeLineEbene1 = 2f;
	private float dickeLineEbene2 = 2f;
	private float dickeLineEbene3 = 2f;
	private float dickeBaseline = 2f;

	private Paint paint = null;

	private final float scalaSweep;
	private final float startWinkel;
	private float[] linesEbene1;
	private float[] linesEbene2 = null;
	private float[] linesEbene3 = null;

	private FontAttributes attrEbene1 = null;
	private FontAttributes attrEbene2 = null;

	private float fontRadiusEbene1 = 0f;
	private float fontRadiusEbene2 = 0f;

	private String format = "%.0f";

	private boolean invert = false;
	private final float minValue;
	private final float maxValue;

	public SkalaPart(//
			final PointF center, //
			final float radiusLineStart, final float radiusLineEnd, //
			final float startWinkel, final float sweep, //
			final float minValue, final float maxValue, //
			final float[] linesEbene1) {
		c = center;
		// Radien berechnen
		setupLineRadien(radiusLineStart, radiusLineEnd);
		setLinesEbene1(linesEbene1);
		setupDefaultPaint();

		this.minValue = minValue;
		this.maxValue = maxValue;
		scalaSweep = sweep;
		this.startWinkel = startWinkel;
		fontRadiusEbene1 = radiusLineEnd * 1.01f;
		fontRadiusEbene2 = 0f;
	}

	// ###############################################################################################
	private SkalaPart setupDefaultPaint() {
		paint = PaintProvider.initScalePaint();
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL);
		return this;
	}

	// ###############################################################################################
	public SkalaPart setupLineRadien(final float radiusLineStart, final float radiusLineEnd) {
		radiusMain = radiusLineStart;
		radiusLineEbene1 = radiusLineEnd;
		final float diffRad = radiusLineEbene1 - radiusMain;
		radiusLineEbene2 = radiusMain + diffRad * 0.66f;
		radiusLineEbene3 = radiusMain + diffRad * 0.33f;
		return this;
	}

	public SkalaPart setupLineRadienAllTheSame() {
		radiusLineEbene2 = radiusLineEbene1;
		radiusLineEbene3 = radiusLineEbene1;
		return this;
	}

	// ###############################################################################################
	private SkalaPart setupBaseLineRadius(final float radiusMain) {
		radiusBaseLine = radiusMain;
		return this;
	}

	public SkalaPart setupDefaultBaseLineRadius() {
		setupBaseLineRadius(radiusMain);
		return this;
	}

	// ###############################################################################################
	public SkalaPart setLinesEbene1(final float[] scala) {
		linesEbene1 = scala;
		return this;
	}

	public SkalaPart setLinesEbene2(final float[] scala) {
		linesEbene2 = scala;
		return this;
	}

	public SkalaPart setLinesEbene3(final float[] scala) {
		linesEbene3 = scala;
		return this;
	}

	// ###############################################################################################
	public SkalaPart setFontAttributesEbene1(final FontAttributes attr) {
		attrEbene1 = attr;
		return this;
	}

	public SkalaPart setFontAttributesEbene2(final FontAttributes attr) {
		attrEbene2 = attr;
		return this;
	}

	public SkalaPart setFontAttributesEbene2Default() {
		attrEbene2 = attrEbene1.clone();
		// Fontsize anpassen (etwas kleiner)
		attrEbene2.setFontSize(attrEbene1.getFontSize() * 0.8f);
		// Radius setzen
		setFontRadiusEbene2Default();
		return this;
	}

	// ###############################################################################################
	public SkalaPart setFontRadiusEbene1(final float radius) {
		fontRadiusEbene1 = radius;
		return this;
	}

	public SkalaPart setFontRadiusEbene2(final float radius) {
		fontRadiusEbene2 = radius;
		return this;
	}

	public SkalaPart setFontRadiusEbene2Default() {
		fontRadiusEbene2 = fontRadiusEbene1;
		return this;
	}

	// ###############################################################################################
	public SkalaPart setNumberFormat(final String format) {
		this.format = format;
		return this;
	}

	public SkalaPart setNumberFormatNachkommaOhne() {
		format = "%.0f";
		return this;
	}

	public SkalaPart setNumberFormatNachkomma1() {
		format = "%.1f";
		return this;
	}

	public SkalaPart setNumberFormatNachkomma2() {
		format = "%.2f";
		return this;
	}

	// ###############################################################################################
	public SkalaPart setDicke(final float dicke) {
		dickeLineEbene1 = dicke;
		dickeLineEbene2 = dicke * 0.8f;
		dickeLineEbene3 = dicke * 0.6f;
		return this;
	}

	public SkalaPart setDickeBaseLine(final float dicke) {
		dickeBaseline = dicke;
		return this;
	}

	public SkalaPart setDickeBaseLineDefault() {
		dickeBaseline = dickeLineEbene1;
		return this;
	}

	// ###############################################################################################
	public SkalaPart invertText(final boolean invert) {
		this.invert = invert;
		return this;
	}

	public void draw(final Canvas canvas) {

		drawLinesForEbene(canvas, linesEbene1, radiusMain, radiusLineEbene1, fontRadiusEbene1, dickeLineEbene1, attrEbene1);
		drawLinesForEbene(canvas, linesEbene2, radiusMain, radiusLineEbene2, fontRadiusEbene2, dickeLineEbene2, attrEbene2);
		drawLinesForEbene(canvas, linesEbene3, radiusMain, radiusLineEbene3, 0f, dickeLineEbene3, null);

		// ggf linie zeichnen
		if (radiusBaseLine > 0) {
			final RectF oval = GeometrieHelper.getCircle(c, radiusBaseLine);
			final Path mArc = new Path();
			mArc.addArc(oval, startWinkel, scalaSweep);
			paint.setStyle(Style.STROKE);
			paint.setStrokeWidth(dickeBaseline);
			canvas.drawPath(mArc, paint);
		}

	}

	private void drawLinesForEbene(final Canvas canvas, final float[] lines, //
			final float radiusStart, final float radiusEnd, final float fontRadius, //
			final float dicke, //
			final FontAttributes attr) {
		// Fontattribute setzen
		if (attr != null) {
			attr.setupPaint(paint);
		}
		// Linien Zeichnen
		if (lines != null && lines.length > 0) {
			final float valueSweep = maxValue - minValue;
			for (final float s : lines) {
				final float valueDiff = s - minValue;
				final float sweep = scalaSweep / valueSweep * valueDiff;
				final float winkel = startWinkel + sweep;
				final Path path = new ZeigerShapePath(c, radiusStart, radiusEnd, dicke, winkel, ZEIGER_TYP.rect);
				canvas.drawPath(path, paint);
				// Nummern zeichnen
				if (attr != null) {
					int faktor = 1;
					if (invert) {
						faktor = -1;
					}

					final Path mArc = new Path();
					final RectF oval = GeometrieHelper.getCircle(c, fontRadius);
					mArc.addArc(oval, winkel - 18 * faktor, 36 * faktor);
					final String nr = String.format(Locale.US, format, s);
					canvas.drawTextOnPath(nr, mArc, 0, 0, paint);
				}
			}
		}
	}

}
