package de.geithonline.abattlwp.bitmapdrawer.parts;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import de.geithonline.abattlwp.bitmapdrawer.data.DropShadow;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZColoring;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZMode;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZStyle;
import de.geithonline.abattlwp.bitmapdrawer.shapes.RectPath;
import de.geithonline.abattlwp.settings.PaintProvider;

public class LevelPartRectangular {

	private EZStyle style = EZStyle.sweep;
	private EZMode modus = EZMode.Einer;
	private final PointF c;
	private Paint paint;
	private int levelIntern;
	private float abstandZwischenSegemten = 1.5f;
	private float anzahlSegmente = 10;

	private float strokeWidthSegmente = 1f;
	private final int level;
	private final EZColoring coloring;
	private final RectF rect;
	private final float maxHight;

	public LevelPartRectangular(final PointF center, final RectF rect, //
			final int level, final EZColoring coloring) {
		c = center;
		this.rect = rect;
		maxHight = rect.bottom - rect.top;
		this.level = level;
		this.coloring = coloring;
		setMode(modus);
		switch (coloring) {
			default:
			case LevelColors:
				paint = PaintProvider.getBatteryPaint(this.level);
				break;
			case Colorfull:
			case Custom:
			case ColorOf100:
				paint = PaintProvider.getBatteryPaint(100);
				break;
		}
		initPaint();
	}

	private void initPaint() {
		paint.setStrokeWidth(strokeWidthSegmente);
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL);
	}

	public LevelPartRectangular setColor(final int color) {
		paint.setColor(color);
		return this;
	}

	public LevelPartRectangular setDropShadow(final DropShadow dropShadow) {
		if (dropShadow != null) {
			dropShadow.setUpPaint(paint);
		}
		return this;
	}

	public LevelPartRectangular setMode(final EZMode modus) {
		this.modus = modus;
		switch (this.modus) {
			default:
			case Einer:
				levelIntern = level;
				anzahlSegmente = 100;
				break;
			case EinerOnly9Segmente:
				levelIntern = level % 10;
				anzahlSegmente = 9;
				break;
			case EinerOnly10Segmente:
				levelIntern = level % 10;
				anzahlSegmente = 10;
				break;
			case Zweier:
				levelIntern = level / 2;
				anzahlSegmente = 50;
				break;
			case Vierer:
				levelIntern = level / 4;
				anzahlSegmente = 25;
				break;
			case Fuenfer:
				levelIntern = level / 5;
				anzahlSegmente = 20;
				break;
			case FuenfUndZwanziger:
				levelIntern = level / 25;
				anzahlSegmente = 4;
				break;
			case Zwanziger:
				levelIntern = level / 20;
				anzahlSegmente = 5;
				break;
			case Zehner:
				levelIntern = level / 10;
				anzahlSegmente = 10;
				break;
		}
		return this;
	}

	public LevelPartRectangular setStyle(final EZStyle style) {
		this.style = style;
		return this;
	}

	/**
	 * @param abstandsWinkel
	 *            in grad (default ist 1.5 grad)
	 * @return
	 */
	public LevelPartRectangular setSegemteAbstand(final float abstandsWinkel) {
		abstandZwischenSegemten = abstandsWinkel;
		return this;
	}

	public LevelPartRectangular setStrokeWidth(final float strokWidth) {
		strokeWidthSegmente = strokWidth;
		paint.setStrokeWidth(strokeWidthSegmente);
		return this;
	}

	public void draw(final Canvas canvas) {
		switch (style) {
			default:
			case sweep:
				drawSweep(canvas);
				break;
			case sweep_withAplpah:
				drawSweepWithAlpha(canvas);
				break;
			case sweep_withBackgroundPaint:
				drawSweepWithBackground(canvas);
				break;
			case sweep_withOutline:
				drawSweepWithOutline(canvas);
				break;
			case segmented_onlyactive:
				drawSegemtedOnlyAct(canvas);
				break;
			case segmented_all:
				drawSegemtedAll(canvas);
				break;
			case segmented_all_alpha:
				drawSegemtedAllHalfAlpha(canvas);
				break;
			case segmented_all_backgroundPaint:
				drawSegemtedAllBackgroundPaint(canvas);
				break;
		}
	}

	private void drawSweepWithOutline(final Canvas canvas) {
		// level
		drawSweep(canvas);
		// outline um alles
		final Path pathOutline = new RectPath(rect);
		paint.setStyle(Style.STROKE);
		canvas.drawPath(pathOutline, paint);
	}

	private void drawSweepWithAlpha(final Canvas canvas) {
		final float levelHight = drawSweep(canvas);
		// den rest
		final int alpha = paint.getAlpha();
		paint.setColor(PaintProvider.getColorForLevel(100));
		paint.setAlpha(alpha / 3);
		final RectF restRect = new RectF(rect);
		restRect.bottom = rect.bottom - levelHight;
		final Path restPath = new RectPath(restRect);
		canvas.drawPath(restPath, paint);
	}

	private void drawSweepWithBackground(final Canvas canvas) {
		final float levelHight = drawSweep(canvas);
		// den rest
		final RectF restRect = new RectF(rect);
		restRect.bottom = rect.bottom - levelHight;
		final Path restPath = new RectPath(restRect);
		canvas.drawPath(restPath, PaintProvider.getBackgroundPaint());
	}

	private float drawSweep(final Canvas canvas) {
		final float distanceProSegment = maxHight / anzahlSegmente;
		final float levelHight = distanceProSegment * levelIntern;
		paint.setStyle(Style.FILL);
		// Level
		final RectF levelRect = new RectF(rect);
		levelRect.top = levelRect.bottom - levelHight;
		final Path levelPath = new RectPath(levelRect);
		canvas.drawPath(levelPath, paint);
		return levelHight;
	}

	private void drawSegemtedOnlyAct(final Canvas canvas) {
		final float segmentHight = (maxHight - (anzahlSegmente) * abstandZwischenSegemten) / anzahlSegmente;
		final float abstandSegmente = abstandZwischenSegemten + segmentHight;
		// alpha merken
		final int alpha = paint.getAlpha();
		for (int i = 0; i < anzahlSegmente; i = i + 1) {
			final RectF segmentRect = new RectF(rect);
			segmentRect.bottom = rect.bottom - abstandZwischenSegemten / 2 - i * abstandSegmente;
			segmentRect.top = segmentRect.bottom - segmentHight;

			if (i < levelIntern) {
				if (coloring.equals(EZColoring.Colorfull)) {
					final int faktor = (int) (100 / anzahlSegmente);
					paint.setColor(PaintProvider.getColorForLevel(i * faktor));
				}
				// für den Colorfull Fall wieder setzen von Alpha!
				paint.setAlpha(alpha);
				final Path path = new RectPath(segmentRect);
				canvas.drawPath(path, paint);
			} else {
				// do nothing
			}
		}
	}

	private void drawSegemtedAll(final Canvas canvas) {
		final float segmentHight = (maxHight - (anzahlSegmente) * abstandZwischenSegemten) / anzahlSegmente;
		final float abstandSegmente = abstandZwischenSegemten + segmentHight;
		// alpha merken
		final int alpha = paint.getAlpha();
		for (int i = 0; i < anzahlSegmente; i = i + 1) {
			final RectF segmentRect = new RectF(rect);
			segmentRect.bottom = rect.bottom - abstandZwischenSegemten / 2 - i * abstandSegmente;
			segmentRect.top = segmentRect.bottom - segmentHight;

			if (i < levelIntern) {
				paint.setStyle(Style.FILL);
			} else {
				paint.setStyle(Style.STROKE);
			}
			if (coloring.equals(EZColoring.Colorfull)) {
				final int faktor = (int) (100 / anzahlSegmente);
				paint.setColor(PaintProvider.getColorForLevel(i * faktor));
			}
			// für den Colorfull Fall wieder setzen von Alpha!
			paint.setAlpha(alpha);
			final Path path = new RectPath(segmentRect);
			canvas.drawPath(path, paint);
		}
	}

	private void drawSegemtedAllHalfAlpha(final Canvas canvas) {
		final float segmentHight = (maxHight - (anzahlSegmente) * abstandZwischenSegemten) / anzahlSegmente;
		final float abstandSegmente = abstandZwischenSegemten + segmentHight;
		// alpha merken
		final int alpha = paint.getAlpha();
		for (int i = 0; i < anzahlSegmente; i = i + 1) {
			final RectF segmentRect = new RectF(rect);
			segmentRect.bottom = rect.bottom - abstandZwischenSegemten / 2 - i * abstandSegmente;
			segmentRect.top = segmentRect.bottom - segmentHight;

			if (i < levelIntern) {
				paint.setAlpha(alpha);
			} else {
				paint.setAlpha(alpha / 3);
			}
			if (coloring.equals(EZColoring.Colorfull)) {
				final int faktor = (int) (100 / anzahlSegmente);
				paint.setColor(PaintProvider.getColorForLevel(i * faktor));
			}
			// für den Colorfull Fall wieder setzen von Alpha!
			paint.setAlpha(alpha);
			final Path path = new RectPath(segmentRect);
			canvas.drawPath(path, paint);
		}
	}

	private void drawSegemtedAllBackgroundPaint(final Canvas canvas) {
		final float segmentHight = (maxHight - (anzahlSegmente) * abstandZwischenSegemten) / anzahlSegmente;
		final float abstandSegmente = abstandZwischenSegemten + segmentHight;
		// alpha merken
		final int alpha = paint.getAlpha();
		for (int i = 0; i < anzahlSegmente; i = i + 1) {
			final RectF segmentRect = new RectF(rect);
			segmentRect.bottom = rect.bottom - abstandZwischenSegemten / 2 - i * abstandSegmente;
			segmentRect.top = segmentRect.bottom - segmentHight;

			if (i < levelIntern) {
				if (coloring.equals(EZColoring.Colorfull)) {
					final int faktor = (int) (100 / anzahlSegmente);
					paint.setColor(PaintProvider.getColorForLevel(i * faktor));
				}
				// für den Colorfull Fall wieder setzen von Alpha!
				paint.setAlpha(alpha);
				final Path path = new RectPath(segmentRect);
				canvas.drawPath(path, paint);
			} else {
				// für den Colorfull Fall wieder setzen von Alpha!
				final Path path = new RectPath(segmentRect);
				canvas.drawPath(path, PaintProvider.getBackgroundPaint());
			}
		}
	}

}
