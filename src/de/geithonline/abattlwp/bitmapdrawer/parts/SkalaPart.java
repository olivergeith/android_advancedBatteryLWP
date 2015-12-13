package de.geithonline.abattlwp.bitmapdrawer.parts;

import java.util.Locale;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import de.geithonline.abattlwp.bitmapdrawer.data.FontAttributes;
import de.geithonline.abattlwp.bitmapdrawer.data.SkalaData;
import de.geithonline.abattlwp.bitmapdrawer.data.SkalaLines;
import de.geithonline.abattlwp.bitmapdrawer.shapes.ZeigerShapePath;
import de.geithonline.abattlwp.bitmapdrawer.shapes.ZeigerShapePath.ZEIGER_TYP;
import de.geithonline.abattlwp.settings.Settings;
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

	private Paint linePaint = null;
	private Paint textPaint = null;

	private final SkalaData scala;

	private final SkalaLines skalaLines;

	private FontAttributes attrEbene1 = null;
	private FontAttributes attrEbene2 = null;
	private float fontRadiusEbene1 = 0f;
	private float fontRadiusEbene2 = 0f;

	private boolean dontWriteOuterNumbers = false;

	private String format = "%.0f";
	private boolean invert = false;

	protected SkalaPart(//
			final PointF center, //
			final float radiusLineStart, final float radiusLineEnd, //
			final SkalaData scale, //
			final SkalaLines skalaLines) {
		c = center;
		this.skalaLines = skalaLines;
		scala = scale;
		fontRadiusEbene1 = radiusLineEnd * 1.02f;
		fontRadiusEbene2 = 0f;
		// Radien berechnen
		setupLineRadien(radiusLineStart, radiusLineEnd);
		setupDefaultLinePaint();
		setupDefaultTextPaint();
	}

	public SkalaData getScala() {
		return scala;
	}

	// ###############################################################################################
	public SkalaPart dontWriteOuterNumbers() {
		dontWriteOuterNumbers = true;
		return this;
	}

	// ###############################################################################################
	private SkalaPart setupDefaultLinePaint() {
		linePaint = new Paint();
		linePaint.setAlpha(255);
		linePaint.setFakeBoldText(true);
		linePaint.setAntiAlias(true);
		linePaint.setStyle(Style.FILL);
		linePaint.setColor(Settings.getScaleColor());
		return this;
	}

	private SkalaPart setupDefaultTextPaint() {
		textPaint = new Paint();
		textPaint.setAlpha(255);
		textPaint.setFakeBoldText(true);
		textPaint.setAntiAlias(true);
		textPaint.setStyle(Style.FILL);
		textPaint.setColor(Settings.getScaleTextColor());
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

	public SkalaPart draw(final Canvas canvas) {

		drawLinesForEbene(canvas, skalaLines.ebene1, radiusMain, radiusLineEbene1, fontRadiusEbene1, dickeLineEbene1, attrEbene1);
		drawLinesForEbene(canvas, skalaLines.ebene2, radiusMain, radiusLineEbene2, fontRadiusEbene2, dickeLineEbene2, attrEbene2);
		drawLinesForEbene(canvas, skalaLines.ebene3, radiusMain, radiusLineEbene3, 0f, dickeLineEbene3, null);

		// ggf linie zeichnen
		if (radiusBaseLine > 0) {
			final RectF oval = GeometrieHelper.getCircle(c, radiusBaseLine);
			final Path mArc = new Path();
			mArc.addArc(oval, scala.startWinkel, scala.scalaSweep);
			linePaint.setStyle(Style.STROKE);
			linePaint.setStrokeWidth(dickeBaseline);
			canvas.drawPath(mArc, linePaint);
		}
		return this;
	}

	private void drawLinesForEbene(final Canvas canvas, final float[] lines, //
			final float radiusStart, final float radiusEnd, final float fontRadius, //
			final float dicke, //
			final FontAttributes attr) {
		// Fontattribute setzen
		if (attr != null) {
			attr.setupPaint(textPaint);
		}
		// Linien Zeichnen
		if (lines != null && lines.length > 0) {
			final float valueSweep = scala.maxValue - scala.minValue;
			for (final float s : lines) {
				final float valueDiff = s - scala.minValue;
				final float sweep = scala.scalaSweep / valueSweep * valueDiff;
				final float winkel = scala.startWinkel + sweep;
				final Path path = new ZeigerShapePath(c, radiusStart, radiusEnd, dicke, winkel, ZEIGER_TYP.rect);
				canvas.drawPath(path, linePaint);

				// Nummern zeichnen
				if (attr != null) {
					if ((winkel == scala.startWinkel || winkel == scala.startWinkel + scala.scalaSweep) //
							&& dontWriteOuterNumbers == true) {
						// do nothing
					} else {
						int faktor = 1;
						if (invert) {
							faktor = -1;
						}
						final Path mArc = new Path();
						final RectF oval = GeometrieHelper.getCircle(c, fontRadius);
						mArc.addArc(oval, winkel - 18 * faktor, 36 * faktor);
						final String nr = String.format(Locale.US, format, s);
						canvas.drawTextOnPath(nr, mArc, 0, 0, textPaint);
					}
				}
			}
		}
	}

}
